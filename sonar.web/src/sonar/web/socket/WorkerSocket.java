package sonar.web.socket;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.Session;

public class WorkerSocket extends Endpoint {
    
    private static Set<Session> userSessions;
    
    static {
    	userSessions = Collections.synchronizedSet(new HashSet<Session>());
    }
    
    public WorkerSocket() {
    	System.out.println("Started WebSocket");
    }

    @OnClose
    public void onClose(Session userSession) {
        System.out.println("Connection closed. Id: " + userSession.getId());
        userSessions.remove(userSession);
    }

    /*public void onMessage2(String message, Session userSession) {
        System.out.println("Message Received: " + message);
        for (Session session : userSessions) {
            System.out.println("Sending to " + session.getId());
            session.getAsyncRemote().sendText(message);
        }
        //Object ret = CommandExecutor.execute(message, null);
        Object ret = new TestClass(10, "test");
        Gson gson = new Gson();
        String s = gson.toJson(ret);
        System.out.println(s);
        userSession.getAsyncRemote().sendText(s);
    }*/

	@Override
	public void onOpen(Session session, EndpointConfig cfg) {
		userSessions.add(session);
		ClientMessageProcessor processor = new ClientMessageProcessor(session);
		session.addMessageHandler(processor);
	}
	
	@Override
	public void onClose(Session session, CloseReason closeReason) {
		super.onClose(session, closeReason);
	}
	
	@Override
	public void onError(Session session, Throwable thr) {
		super.onError(session, thr);
	}
}