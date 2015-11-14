package sonar.core.internal;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;

import sonar.core.CommandInfo;
import sonar.core.annotations.Command;
import sonar.core.annotations.CommandExecution;
import sonar.core.service.CommandStorageService;

public class CommandBundleStorageServce implements CommandStorageService {

	private static final Map<String, CommandInfo> models;
	private static final Map<Bundle, List<String>> bundleModels;
	
	static {
		models = new HashMap<String, CommandInfo>();
		bundleModels = new HashMap<Bundle, List<String>>();
	}
	
	public CommandBundleStorageServce() {
		System.out.println("CommandBundleStorageServce started");
	}
	
	@Override
	public Object execute(String cmdName, String param) {
		return executeInternal(cmdName, param);
	}
	
	public static Object executeInternal(String cmdName, String param) {
		if (!models.containsKey(cmdName))
			return null;
		CommandInfo ci = models.get(cmdName);
		if (ci.isClientScript())
			return null;
		return execute(ci, param);
	}
	
	protected static Object execute(CommandInfo ci, String param) {
		Class<?> cmdClass = ci.getCmdClass();
		if (cmdClass == null)
			return null;
		//check all methods
		for(Method m : cmdClass.getDeclaredMethods()) {
			//and find annotation on method
			CommandExecution ce = m.getAnnotation(CommandExecution.class);
			if (ce == null)
				continue;
			try {
				Object cmd = cmdClass.newInstance();
				Object prm = createParameter(m, param);
				Object ret = null;
				if (m.getParameterTypes().length == 1)
					ret = m.invoke(cmd, prm);
				else if (m.getParameterTypes().length == 0)
					ret = m.invoke(cmd);
				else
					return null;
				return ret;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
	private static Object createParameter(Method method, String param) {
		if (method == null || param == null)
			return null;
		Class<?>[] paramTypes = method.getParameterTypes();
		//no parameters
		if (paramTypes == null || paramTypes.length == 0)
			return null;
		//not good. too many parameters in method
		if (paramTypes.length > 1) {
			return null;
		}
		Class<?> clazz =paramTypes[0];
		Object ret = JsonConverter.toObject(clazz, param);
		return ret;
	}

	public static void removeBundle(Bundle bundle) {
		if (!bundleModels.containsKey(bundle))
			return;
		List<String> cmds = bundleModels.get(bundle);
		for (String cmd : cmds) {
			if (models.containsKey(cmd))
				models.remove(cmd);
		}
		bundleModels.remove(bundle);
	}
	

	public static void processBundle(Bundle bundle) {
		List<CommandInfo> list = loadCommands(bundle);
		Map<CommandInfo, String> cmds = new HashMap<CommandInfo, String>();
		for(CommandInfo cls : list) {
			cmds.put(cls, cls.getName());
		}
		if (!bundleModels.containsKey(bundle)) {
			bundleModels.put(bundle, new ArrayList<String>(cmds.values()));
			for(CommandInfo cls : list) {
				models.put(cls.getName(), cls);
			}
		} /*else {
			List<String> existsCmds = bundleModels.get(bundle);
			Iterator<CommandInfo> it =  cmds.keySet().iterator();
			while (it.hasNext()) {
				CommandInfo clz = it.next();
				String cmd =cmds.get(clz);
				if (existsCmds.contains(cmd))
					continue;
				existsCmds.add(cmd);
				models.put(cmd, clz);
			}
			for (String cmd : existsCmds) {
				if (cmds.values().contains(cmd))
					continue;
				existsCmds.remove(cmd);
				models.remove(cmd);
			}
		}*/
	}
	
	private static List<CommandInfo> loadCommands(Bundle bundle) {
		List<CommandInfo> list = new ArrayList<CommandInfo>();
		Enumeration<URL> items= bundle.findEntries("/", "*.class", true);
		while (items.hasMoreElements()) {
			URL item = items.nextElement();
			String fn = item.getFile();
			//hack for debug mode
			if (fn.startsWith("/bin/"))
				continue;
			else if (fn.startsWith("/target/classes/"))
				fn = fn.substring(15);
			//System.out.println(fn);
			String className = fn.replace(".class", "").replace("/", ".");
			className = className.substring(1);
			Class<?> clazz;
			try {
				clazz = bundle.loadClass(className);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			Command cmd = clazz.getAnnotation(Command.class);
			if (cmd != null) {
				CommandInfo ci = new CommandInfo();
				ci.setCmdClass(clazz);
				ci.setScriptName(cmd.script());
				list.add(ci);
				System.out.println("Found command [" + ci.getCmdClass().getCanonicalName() + "]");
			}
		}
		return list;
	}
	
	public static List<String> getClientCommands() {
		List<String> res = new ArrayList<String>();
		for (CommandInfo ci : models.values()) {
			if (!ci.isClientScript())
				continue;
			res.add(ci.getName());
		}
		return res;
	}
	
	public static CommandInfo getClientScript(String cmdName) {
		if (!models.containsKey(cmdName))
			return null;
		CommandInfo ci = models.get(cmdName);
		if (!ci.isClientScript())
			return null;
		return ci;
	}
	
}
