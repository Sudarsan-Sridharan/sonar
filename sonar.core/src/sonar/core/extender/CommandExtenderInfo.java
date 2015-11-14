package sonar.core.extender;

/**
 * Extender maps command result and model placeholder to store result
 * @author 12
 *
 */
public class CommandExtenderInfo extends ExtenderInfo {
	
	/**
	 * Command class
	 */
	private String cmdClass;
	/**
	 * PlaceHolder name;
	 */
	private String placeHolderName;
	
	private String modelClass;
	
	public CommandExtenderInfo(Class<?> cmd, String holder) {
		setCmdClass(cmd.getCanonicalName());
		setPlaceHolderName(holder);
	}
	
	public CommandExtenderInfo(Class<?> cmd, String holder, Class<?> model) {
		setCmdClass(cmd.getCanonicalName());
		setPlaceHolderName(holder);
		setModel(model);
	}
	
	public String getCmdClass() {
		return cmdClass;
	}
	public void setCmdClass(String cmdClass) {
		this.cmdClass = cmdClass;
	}
	public String getPlaceHolderName() {
		return placeHolderName;
	}
	public void setPlaceHolderName(String placeHolderName) {
		this.placeHolderName = placeHolderName;
	}

	public String getModel() {
		return modelClass;
	}

	public void setModel(Class<?> model) {
		if (model == null)
			return;
		this.modelClass = model.getCanonicalName();
	}

}
