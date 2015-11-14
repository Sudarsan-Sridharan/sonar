package sonar.test.demoexec;

import sonar.core.Component;
import sonar.core.service.ExecutionService;
import sonar.core.service.ModelStorageService;

public class TestClass {
	
	private ModelStorageService modelService;
	
	public TestClass() {
		System.out.println("[TestClass] started");
	}

	public void register(ModelStorageService service) {
		System.out.println("[TestClass] registered " + service.getClass().getCanonicalName());
		modelService = service;
		loadModel();
	}
	
	public void unregister(ModelStorageService service) {
		if (modelService == service)
			modelService = null;
	}
	
	private void loadModel() {
		String data = "{\"id\":\"1\", \"name\":\"sonar.core.internal.command.LoadComponent\", \"param\":\"sonar.test.SampleModel\"}";
		String comp = ExecutionService.processRequest(data);
		System.out.println(comp);
		/*Component comp = modelService.createComponent("sonar.test.SampleModel");
		Component comp2 = modelService.createComponent("sonar.test.SampleModel2");
		System.out.println(comp2.getName());*/
	}
}
