package sonar.core.service;

import sonar.core.Component;

/**
 *	Create new component
 */
public interface ModelStorageService {
	Component createComponent(String name);
}
