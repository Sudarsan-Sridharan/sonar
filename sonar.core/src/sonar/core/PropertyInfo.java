package sonar.core;

import sonar.core.annotations.Property;

public class PropertyInfo {
	private String name;
	private String target;
	private PropertyMethod method;
	private String attr;
	
	public PropertyInfo(Property prop) {
		name = prop.name();
		target = prop.target();
		method = prop.method();
		attr = prop.attr();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public PropertyMethod getMethod() {
		return method;
	}
	public void setMethod(PropertyMethod method) {
		this.method = method;
	}
	public String getAttr() {
		return attr;
	}
	public void setAttr(String attr) {
		this.attr = attr;
	}
	
	@Override
	public String toString() {
		if (attr == null || attr.length() == 0)
			return String.format("[%s] %s", method, name);
		else
			return String.format("[%s] %s (%s)", method, name, attr);
	}
}
