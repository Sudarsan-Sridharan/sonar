package sonar.core.internal;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;

import sonar.core.annotations.Extender;
import sonar.core.extender.ExtenderInfo;
import sonar.core.extender.ModelExtender;

public class ModelExtenderBundleService {
	/**
	 * model to extender list
	 */
	private static final Map<Class<?>, List<Class<?>>> modelExtenders;
	/**
	 * bundle to model list
	 */
	private static final Map<Bundle, List<Class<?>>> bundleModels;
	
	static {
		modelExtenders = new HashMap<Class<?>, List<Class<?>>>();
		bundleModels = new HashMap<Bundle, List<Class<?>>>();
	}
	
	public ModelExtenderBundleService() {
		System.out.println("ModelExtenderBundleService started");
	}
	
	public static void removeBundle(Bundle bundle) {
		if (!bundleModels.containsKey(bundle))
			return;
		List<Class<?>> exts = bundleModels.get(bundle);
		for (Class<?> cmd : exts) {
			if (modelExtenders.containsKey(cmd))
				modelExtenders.remove(cmd);
		}
		bundleModels.remove(bundle);
	}
	
	public static void processBundle(Bundle bundle) {
		List<Class<?>> bundleExtenders;
		if (bundleModels.containsKey(bundle)) {
			bundleExtenders = bundleModels.get(bundle);
		} else {
			bundleExtenders = new ArrayList<Class<?>>();
			bundleModels.put(bundle, bundleExtenders);
		}
		Map<Class<?>, List<Class<?>>> list = loadExtenders(bundle);
		Set<Class<?>> models = list.keySet();
		for (Class<?> model : models) {
			loadModel(model, list.get(model));
		}
	}
	
	private static void loadModel(Class<?> model, List<Class<?>> exts) {
		for(Class<?> ext :exts) {
			if (!ModelExtender.class.isAssignableFrom(ext))
				continue;
			List<Class<?>> modelExs;
			if (modelExtenders.containsKey(model)) {
				modelExs = modelExtenders.get(model);
			} else {
				modelExs = new ArrayList<Class<?>>();
				modelExtenders.put(model, modelExs);
			}
			modelExs.addAll(exts);
		}
	}
	
	/**
	 * Loads extenders from bundle
	 * @param bundle bundle to parse
	 * @return model to extender list
	 */
	private static Map<Class<?>, List<Class<?>>> loadExtenders(Bundle bundle) {
		 Map<Class<?>, List<Class<?>>> list = new  HashMap<Class<?>, List<Class<?>>>();
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
			Extender m = clazz.getAnnotation(Extender.class);
			if (m != null) {
				List<Class<?>> coll = null;
				if (list.containsKey(m.model())) {
					coll = list.get(m.model());
				} else {
					coll = new ArrayList<Class<?>>();
					list.put(m.model(), coll);
				}
				if (!coll.contains(clazz))
					coll.add(clazz);
			}
		}
		return list;
	}
	
	public static List<ExtenderInfo> getExtenders(Class<?> model) {
		List<ExtenderInfo> info = new ArrayList<ExtenderInfo>();
		if (modelExtenders.containsKey(model)) {
			List<Class<?>> exts = modelExtenders.get(model);
			if (exts.size() > 0) {
				for(Class<?> ext : exts) {
					List<ExtenderInfo> ei = getExtendersFromClass(ext);
					info.addAll(ei);
				}
			}
		}
		return info;
	}
	
	private static List<ExtenderInfo> getExtendersFromClass(Class<?> ext) {
		List<ExtenderInfo> ei = new ArrayList<ExtenderInfo>();
		
		try {
			ModelExtender me = (ModelExtender)ext.newInstance();
			ei.addAll(me.get());
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return ei;
	}
}
