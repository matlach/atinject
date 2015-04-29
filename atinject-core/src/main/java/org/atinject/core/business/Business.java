package org.atinject.core.business;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target({ ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface Business {

	Class<? extends BusinessExecutor<?, ?>> executedBy();
}
