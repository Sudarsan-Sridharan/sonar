package sonar.core.service;

public interface CommandStorageService {
	Object execute(String cmd, String param);
}
