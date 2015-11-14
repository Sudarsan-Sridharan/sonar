package sonar.web.config;

import java.util.Collections;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

import org.osgi.framework.FrameworkUtil;

import sonar.web.socket.WorkerSocket;

public class WebSocketApplicationConfig implements ServerApplicationConfig {

	  @Override
	  public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> set) {
	    return Collections.emptySet();
	  }

	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> set) {
		Dictionary<String, String> headers = FrameworkUtil.getBundle(getClass()).getHeaders();
		String path = headers.get("X-Socket-Path");
		if (path == null || path.length() == 0)
			path = "/socket";
		final String fpath = new String(path);
		Set<ServerEndpointConfig> config = new HashSet<ServerEndpointConfig>();
		config.add(ServerEndpointConfig.Builder.create(WorkerSocket.class, fpath).build());
		System.out.println(String.format("WebSocket configured as [%s]", path));
	   return config;
	}

}