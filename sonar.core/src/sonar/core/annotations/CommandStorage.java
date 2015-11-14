package sonar.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *Annotation marks CommandFactory and stores packages fo find commands
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandStorage {
	String rootPackage() default "";
}
