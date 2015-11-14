package sonar.core.internal.command;

import sonar.core.Component;
import sonar.core.annotations.Command;
import sonar.core.annotations.CommandExecution;
import sonar.core.internal.ModelBundleStorageService;

@Command
public class LoadComponent {

	@CommandExecution
	public Component execute(String name) {
		if (name == null)
			return null;
		return ModelBundleStorageService.createComponentInternal(name);
	}
	
}
