package sonar.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation marks Model class in MVC pattern
 */
@Target(value = ElementType.TYPE)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface Model {
	/**
	 * Gets or sets view name
	 */
	String view();
	
	String path();
}
