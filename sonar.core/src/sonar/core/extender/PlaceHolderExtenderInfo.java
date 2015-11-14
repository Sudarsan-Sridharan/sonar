package sonar.core.extender;

/**
 * Class creates PlaceHolder for model
 */
public class PlaceHolderExtenderInfo extends ExtenderInfo {
	/**
	 * Internal name
	 */
	private String name;
	/**
	 * xpath query from model
	 */
	private String query;
	
	/**
	 * Type
	 */
	private HolderType type;

	public PlaceHolderExtenderInfo(String holderName, String xquery, HolderType hType) {
		setName(holderName);
		setQuery(xquery);
		setType(hType);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public HolderType getType() {
		return type;
	}

	public void setType(HolderType type) {
		this.type = type;
	}

}
