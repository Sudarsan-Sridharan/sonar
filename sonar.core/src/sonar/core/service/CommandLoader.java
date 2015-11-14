package sonar.core.service;

import java.util.List;

import sonar.core.CommandInfo;

/**
 * Contains methods for work with commands
 *
 */
public interface CommandLoader {

	/**
	 * Gets list of commands in bundle
	 * @return
	 */
	List<CommandInfo> list();
	
	/**
	 * Executes command
	 * @param cmdName command name
	 * @return
	 */
	Object execute(String cmdName, String param);
}
