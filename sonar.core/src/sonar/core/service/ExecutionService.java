package sonar.core.service;

import sonar.core.internal.CommandBundleStorageServce;
import sonar.core.internal.JsonConverter;
import sonar.core.internal.RequestCommand;
import sonar.core.internal.ResultCommand;

/**
 *
 */
public class ExecutionService {
	
	/**
	 * Process client request and return result
	 * @param data client data
	 * @return result
	 */
	public static String processRequest(String data) {
		if (data == null)
			return null;
		RequestCommand rc = (RequestCommand) JsonConverter.toObject(RequestCommand.class, data);
		Object result = CommandBundleStorageServce.executeInternal(rc.getName(), rc.getParam());
		ResultCommand res = new ResultCommand(result);
		res.setId(rc.getId());
		String out = JsonConverter.toJSON(res);
		return out;
	}

}
