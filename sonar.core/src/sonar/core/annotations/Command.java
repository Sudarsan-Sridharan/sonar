package sonar.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation marks command

 */
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	/**
	 * Specified then command is client-side (javascript)<br/>
	 * Returns path to javascript file
	 * @return path to js file in bundle
	 */
	String script() default "";
}
