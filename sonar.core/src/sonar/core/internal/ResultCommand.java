package sonar.core.internal;

/**
 * Результат операции
 *
 */
public class ResultCommand {
	private int id;
	private Object value;
	
	public ResultCommand(Object value) {
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}

	public int getId() {
		return id;
	}

	public void setId(int idv) {
		id = idv;
	}

}
