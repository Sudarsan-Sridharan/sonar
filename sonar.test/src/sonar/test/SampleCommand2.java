package sonar.test;

import sonar.core.annotations.Command;
import sonar.core.annotations.CommandExecution;

@Command
public class SampleCommand2 {

	@CommandExecution
	public Object exec(Info val) {
		if (val == null)
			return null;
		return val.getX() * 10;
	}
}
