package sonar.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import sonar.core.PropertyMethod;

/**
 * Annotation marks Model property and stores key information
 */
@Target(value = { ElementType.FIELD, ElementType.METHOD })
@Retention(value= RetentionPolicy.RUNTIME)
public @interface Property {
	/**
	 * Gets or sets property name.
	 * If empty, default name equal to field name
	 */
	String name() default "";
	/**
	 * Gets or sets target entity name in view
	 */
	String target();
	/**
	 * Gets or sets representation method
	 */
	PropertyMethod method();
	/**
	 * Gets or sets attribute to store value.
	 * Required when <br><code>method = PropertyMethod.Property</code>
	 */
	String attr() default "";
}
