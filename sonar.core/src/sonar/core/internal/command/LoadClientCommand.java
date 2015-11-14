package sonar.core.internal.command;

import org.osgi.framework.FrameworkUtil;

import sonar.core.CommandInfo;
import sonar.core.annotations.Command;
import sonar.core.annotations.CommandExecution;
import sonar.core.internal.CommandBundleStorageServce;
import sonar.core.service.StaticContentService;

@Command
public class LoadClientCommand {

	@CommandExecution
	public String execute(String name) {
		CommandInfo ci = CommandBundleStorageServce.getClientScript(name);
		if (ci == null)
			return null;
		String res = StaticContentService.getContentAsString(FrameworkUtil.getBundle(ci.getCmdClass()), ci.getScriptName());
		return res;
	}
}
