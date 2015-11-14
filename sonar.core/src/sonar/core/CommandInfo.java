package sonar.core;

public class CommandInfo {
	private Class<?> cmdClass;
	private Boolean isClient;
	private String scriptName;
	
	public Class<?> getCmdClass() {
		return cmdClass;
	}
	public void setCmdClass(Class<?> cmdClazz) {
		this.cmdClass = cmdClazz;
	}
	public Boolean isClientScript() {
		return isClient;
	}

	public String getScriptName() {
		return scriptName;
	}
	public void setScriptName(String script) {
		this.scriptName = script;
		isClient = !"".equalsIgnoreCase(script);
	}
	
	public String getName() {
		return cmdClass == null ? null : cmdClass.getCanonicalName();
	}
}
