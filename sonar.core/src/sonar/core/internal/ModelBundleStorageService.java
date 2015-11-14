package sonar.core.internal;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import sonar.core.Component;
import sonar.core.EventBindingInfo;
import sonar.core.PropertyInfo;
import sonar.core.annotations.EventBinding;
import sonar.core.annotations.EventInfo;
import sonar.core.annotations.Model;
import sonar.core.annotations.Property;
import sonar.core.extender.CommandExtenderInfo;
import sonar.core.extender.ExtenderInfo;
import sonar.core.extender.PlaceHolderExtenderInfo;
import sonar.core.service.ModelStorageService;
import sonar.core.service.StaticContentService;

public class ModelBundleStorageService implements ModelStorageService {

	private static final Map<String, Class<?>> models;
	private static final Map<Bundle, List<String>> bundleModels;
	
	static {
		models = new HashMap<String, Class<?>>();
		bundleModels = new HashMap<Bundle, List<String>>();
	}
	
	public ModelBundleStorageService() {
		System.out.println("ModelBundleStorageService started");
	}
	
	@Override
	public Component createComponent(String name) {
		return createComponentInternal(name);
	}
	
	public static Component createComponentInternal(String name) {
		if (!models.containsKey(name))
			return null;
		Class<?> model = models.get(name);
		return CreateComponent(model);
	}
	
	/**
	 * Creates Component metadata
	 * @param clazz Model class
	 * @return Component metadata
	 */
	protected static Component CreateComponent(Class<?> clazz) {
		Component comp = new Component();
		loadProperties(clazz, comp);
		loadView(clazz, comp);
		loadEvents(clazz, comp);
		loadExtenders(clazz, comp);
		return comp;
	}
	
	/**
	 * Load properties from annotations
	 * @param clazz Model class
	 * @param comp Component instance
	 */
	private static void loadProperties(Class<?> clazz, Component comp) {
		comp.setName(clazz.getCanonicalName());
		Field[] fi = clazz.getDeclaredFields();
		for (Field f : fi) {
			Property an = f.getAnnotation(Property.class);
			if (an == null)
				continue;
			PropertyInfo pi = new PropertyInfo(an);
			if (pi.getName().length() == 0)
				pi.setName(f.getName());
			comp.Properties.add(pi);
		}
	}
	
	/**
	 * Load view content
	 * @param clazz Model class
	 * @param comp Component instance
	 */
	private static void loadView(Class<?> clazz, Component comp) {
		Model m = clazz.getAnnotation(Model.class);
		String str = StaticContentService.getContentAsString(FrameworkUtil.getBundle(ModelBundleStorageService.class).getSymbolicName(), m.path());
		comp.setContent(str);
	}

	/**
	 * Load event binding info
	 * @param clazz Model class
	 * @param comp Component instance
	 */
	private static void loadEvents(Class<?> clazz, Component comp) {
		EventBinding e = clazz.getAnnotation(EventBinding.class);
		if (e == null)
			return;
		for (EventInfo info : e.events()) {
			EventBindingInfo ebi = new EventBindingInfo();
			String clsName = info.cmdClass() == Object.class ? info.cmdName() : info.cmdClass().getCanonicalName();
			ebi.setCmdName(clsName);
			clsName = "".equals(info.elementName()) ? null : info.elementName();
			ebi.setElementName(clsName);
			clsName = "".equals(info.event()) ? null : info.event();
			ebi.setEvent(clsName);
			clsName =info.place();
			if (clsName != "")
				ebi.setPlace(clsName);
			comp.Events.add(ebi);
		}
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
		List<Class<?>> list = loadModels(bundle);
		Map<Class<?>, String> cmds = new HashMap<Class<?>, String>();
		for(Class<?> cls : list) {
			cmds.put(cls, cls.getCanonicalName());
		}
		if (!bundleModels.containsKey(bundle)) {
			bundleModels.put(bundle, new ArrayList<String>(cmds.values()));
			for(Class<?> cls : list) {
				models.put(cls.getCanonicalName(), cls);
			}
		} /*else {
			List<String> existsCmds = bundleModels.get(bundle);
			Iterator<Class<?>> it =  cmds.keySet().iterator();
			while (it.hasNext()) {
				Class<?> clz = it.next();
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
	
	private static List<Class<?>> loadModels(Bundle bundle) {
		List<Class<?>> list = new ArrayList<Class<?>>();
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
			Model m = clazz.getAnnotation(Model.class);
			if (m != null) {
				list.add(clazz);
			}
		}
		return list;
	}
	
	/**
	 * Load model extenders
	 * @param model model class
	 * @param comp component metadata
	 */
	private static void loadExtenders(Class<?> model, Component comp) {
		List<ExtenderInfo> extenders = ModelExtenderBundleService.getExtenders(model);
		for (ExtenderInfo ei : extenders) {
			if (PlaceHolderExtenderInfo.class.isAssignableFrom(ei.getClass())) {
				comp.PlaceHolders.add((PlaceHolderExtenderInfo)ei);
			} else if (CommandExtenderInfo.class.isAssignableFrom(ei.getClass())) {
				comp.CommandBinding.add((CommandExtenderInfo)ei);
			}
		}
	}

}
