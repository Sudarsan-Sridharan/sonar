package sonar.test;

import sonar.core.PropertyMethod;
import sonar.core.annotations.Model;
import sonar.core.annotations.Property;

@Model(view = "SampleView2", path = "Content/SampleView2.html")
public class SampleModel2 {
	@Property(target="result", method = PropertyMethod.Text)
	private String value;
	
	public void setValue(String label) {
		this.value = label;
	}
	
	public String getValue() {
		return value;
	}
}
