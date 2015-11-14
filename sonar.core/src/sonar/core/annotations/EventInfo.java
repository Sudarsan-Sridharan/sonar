package sonar.core.annotations;

/**
 * Annotation contains information about event and command
 */

public @interface EventInfo {
	/**
	 * Reference to command class.
	 * When not specified, using cmdName.
	 * @return command class
	 */
	public Class<?> cmdClass() default Object.class;
	/**
	 * Reference to command name.
	 * When not specified cmdClass, using cmdName.
	 * @return command name
	 */
	public String cmdName() default "";
	/**
	 * Spcified event name which bidts to command
	 * @return event name
	 */
	public String event();
	
	/**
	 * Element name for bind event and command
	 * @return element name
	 */
	public String elementName();
	
	/**
	 * Placeholder name to store model
	 * @return
	 */
	public String place() default "";

}
