package sonar.core.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class StaticContentService {

	public static String getContentAsString(String bundleSymName, String path) {
		Bundle[] bundles = FrameworkUtil.getBundle(StaticContentService.class).getBundleContext().getBundles();
		for (Bundle bundle : bundles) {
			if (!bundle.getSymbolicName().equalsIgnoreCase("sonar.test"))
				continue;
			return getContentAsStringInternal(bundle, path);
		}
		return null;
	}
	
	public static String getContentAsString(Bundle bundle, String path) {
		return getContentAsStringInternal(bundle, path);
	}
	
	private static String getContentAsStringInternal(Bundle bundle, String path) {
		if (bundle == null)
			return null;
		URL url = bundle.getEntry(path);
		try {
			InputStream is = url.openStream();
			Scanner sc = new Scanner(is,"UTF-8");
			sc.useDelimiter("\\A");
			String str = sc.next();
			sc.close();
			is.close();
			return str;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
