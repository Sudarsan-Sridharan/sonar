package sonar.core;

/**
 * Class contains information about event binding
 */
public class EventBindingInfo {
	/**
	 * Command name
	 */
	private String cmdName;
	/**
	 * Event name
	 */
	private String event;
	/**
	 * Element name to bind event
	 */
	private String elementName;
	/**
	 * Place name to store result model after executing command (if required)
	 */
	private String place;
	
	public String getCmdName() {
		return cmdName;
	}
	public void setCmdName(String cmdName) {
		this.cmdName = cmdName;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	
	

}
