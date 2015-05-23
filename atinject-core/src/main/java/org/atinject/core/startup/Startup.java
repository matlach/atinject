package org.atinject.core.startup;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Startup {

	// TODO should be converted to standard @Priority
	int priority() default 0;
	
	public static final int HIGHEST = Integer.MAX_VALUE;
	public static final int LOWEST = Integer.MIN_VALUE;
}
