package sonar.core.internal;

import java.util.HashMap;
import java.util.Map;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.FrameworkUtil;

public class BundleStateListener implements BundleListener {

	private final Bundle ownerBundle;
	private final Map<String, Bundle> activeBundles;
	
	public BundleStateListener() {
		ownerBundle = FrameworkUtil.getBundle(getClass());
		activeBundles = new HashMap<String, Bundle>();
		loadActiveBundles();
	}
	
	@Override
	public void bundleChanged(BundleEvent event) {
		loadActiveBundles();
	}
	
	private void installBundle(Bundle bundle) {
		System.out.println("Find models in " + bundle.getSymbolicName());
		ModelBundleStorageService.processBundle(bundle);
		CommandBundleStorageServce.processBundle(bundle);
		ModelExtenderBundleService.processBundle(bundle);
	}
	
	private void uninstallBundle(Bundle bundle) {
		System.out.println("Remove models from " + bundle.getSymbolicName());
		ModelBundleStorageService.removeBundle(bundle);
		CommandBundleStorageServce.removeBundle(bundle);
		ModelExtenderBundleService.removeBundle(bundle);
	}
	
	private void loadActiveBundles() {
		Bundle[] bundles = ownerBundle.getBundleContext().getBundles();
		for (Bundle bundle : bundles) {
			//if deny autoscan in bundle then exit
			if (bundle.getHeaders().get("X-SONAR-AUTOSCAN") == null)
				continue;
			//bundle unique name
			String name = bundle.getSymbolicName() + "|" + bundle.getVersion().toString();
			//System.out.println(name + " : " + bundle.getState());
			//if bundle active
			if (bundle.getState() == Bundle.ACTIVE || bundle.getState() == Bundle.STARTING) {
				//and I don't know it
				if (!activeBundles.containsKey(name)) {
					//add bundle and process it
					activeBundles.put(name, bundle);
					installBundle(bundle);
				}
				//else if not active
			} else {
				//and I know it
				if (activeBundles.containsKey(name)) {
					//then remove handlers and forget it
					uninstallBundle(bundle);
					activeBundles.remove(name);
				}
			}
		}
	}


}
