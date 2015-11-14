package sonar.test.demoexec;

import sonar.core.service.CommandStorageService;
import sonar.core.service.StaticContentService;

public class TestClass2 {

	private CommandStorageService commandService;
	
	public TestClass2() {
		System.out.println("[TestClass2] started");
	}

	public void register(CommandStorageService service) {
		System.out.println("[TestClass2] registered " + service.getClass().getCanonicalName());
		commandService = service;
		//execute();
		//execute2();
	}
	
	public void unregister(CommandStorageService service) {
		if (commandService == service)
			commandService = null;
	}
	
	private void execute() {
		Object result = commandService.execute("sonar.test.SampleCommand2", "{x:3}");
		System.out.println(result);
	}
	
	private void execute2() {
		String res = StaticContentService.getContentAsString("sonar.test", "/Content/SampleView.html");
		System.out.println(res);
	}
}
