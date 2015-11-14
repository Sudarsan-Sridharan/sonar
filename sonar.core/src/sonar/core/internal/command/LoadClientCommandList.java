package sonar.core.internal.command;

import java.util.List;

import sonar.core.annotations.Command;
import sonar.core.annotations.CommandExecution;
import sonar.core.internal.CommandBundleStorageServce;

@Command
public class LoadClientCommandList {

	@CommandExecution
	public List<String> execute() {
		return CommandBundleStorageServce.getClientCommands();
	}
	
}
