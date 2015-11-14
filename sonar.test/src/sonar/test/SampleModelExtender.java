package sonar.test;

import java.util.ArrayList;
import java.util.List;

import sonar.core.annotations.Extender;
import sonar.core.extender.CommandExtenderInfo;
import sonar.core.extender.ExtenderInfo;
import sonar.core.extender.HolderType;
import sonar.core.extender.ModelExtender;
import sonar.core.extender.PlaceHolderExtenderInfo;

@Extender(model = SampleModel.class)
public class SampleModelExtender implements ModelExtender {

	@Override
	public List<ExtenderInfo> get() {
		List<ExtenderInfo> list = new ArrayList<ExtenderInfo>();
		list.add(new PlaceHolderExtenderInfo("Result", "div[name='results']", HolderType.Single));
		list.add(new CommandExtenderInfo(SampleCommand2.class, "Result", SampleModel2.class));
		return list;
	}

}
