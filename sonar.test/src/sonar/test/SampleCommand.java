package sonar.test;

import sonar.core.annotations.Command;
import sonar.core.annotations.CommandExecution;

@Command
public class SampleCommand {

	@CommandExecution
	public String exec() {
		return "test";
	}
}
