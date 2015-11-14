package sonar.web.socket;

import javax.websocket.MessageHandler;
import javax.websocket.Session;

import sonar.core.service.ExecutionService;

public class ClientMessageProcessor implements MessageHandler.Whole<String> {
	
	private Session _session;
	
	public ClientMessageProcessor(Session session) {
		_session = session;
	}
	
	@Override
	public void onMessage(String message) {
		System.out.println("Recv: " + message);
		String res = ExecutionService.processRequest(message);
		System.out.println("Send: " + res);
        _session.getAsyncRemote().sendText(res);
	}

}