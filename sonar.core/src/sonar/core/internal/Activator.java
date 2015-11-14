package sonar.core.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleListener;

public class Activator implements BundleActivator {

	private static BundleContext ctx;
	private BundleListener listener;
	
	public Activator() {
		listener = new BundleStateListener();
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		ctx = context;
		ctx.addBundleListener(listener);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		ctx.removeBundleListener(listener);
		ctx = null;
	}

}
