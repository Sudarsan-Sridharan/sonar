package sonar.core;

import java.util.ArrayList;
import java.util.List;

import sonar.core.extender.CommandExtenderInfo;
import sonar.core.extender.PlaceHolderExtenderInfo;

public class Component {
	/**
	 * html Content
	 */
	private String content;
	/**
	 * name
	 */
	private String name;
	/**
	 * Properties
	 */
	public List<PropertyInfo> Properties;
	/**
	 * Events
	 */
	public List<EventBindingInfo> Events;
	/**
	 * Placeholders
	 */
	public List<PlaceHolderExtenderInfo> PlaceHolders;
	/**
	 * Command Binding
	 */
	public List<CommandExtenderInfo> CommandBinding;
	
	public Component(String name, String content) {
		this();
		this.name = name;
		this.content = content;
	}
	
	public Component() {
		Properties = new ArrayList<PropertyInfo>();
		Events = new ArrayList<EventBindingInfo>();
		PlaceHolders = new ArrayList<PlaceHolderExtenderInfo>();
		CommandBinding = new ArrayList<CommandExtenderInfo>();
	}
	
	public String getContent() {
		return content;
	}

	public String getName() {
		return name;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setName(String name) {
		this.name = name;
	}
}