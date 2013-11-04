package org.atinject.core.cdi;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * provide replacement for {@link javax.inject.Named} annotation
 * {@link javax.inject.Named} default value is ""
 * {@link javax.inject.Named} value must be unique across the system even if
 * the produced type combined with named annotation is unique (bug ??)
 */
//@Qualifier
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Named
{
    String value() default "";
}
