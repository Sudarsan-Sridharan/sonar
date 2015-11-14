package sonar.test;

import sonar.core.PropertyMethod;
import sonar.core.annotations.EventBinding;
import sonar.core.annotations.EventInfo;
import sonar.core.annotations.Model;
import sonar.core.annotations.Property;

@Model(view = "SampleView", path = "Content/SampleView.html")
@EventBinding(events = {
		@EventInfo(elementName = "process", event = "click", cmdClass = SampleClientCommand.class, place = "results"),
		@EventInfo(elementName = "process2", event = "click", cmdClass = SampleCommand2.class)
})
public class SampleModel {
	@Property(target="text", method = PropertyMethod.Text)
	private String label;
	@Property(target="val", method = PropertyMethod.Property, attr = "value")
	private String value;
	@Property(target="text", method = PropertyMethod.Property, attr = "style.width")
	private String width;

	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	
	
}
