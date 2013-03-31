package org.atinject.core.concurrent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

@InterceptorBinding
@Inherited
@Target( { ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {

    public static final int DEFAULT_RETRY_COUNT = 3;
    public static final int DEFAULT_MIN_TIMEOUT = 100;
    
    int count() default DEFAULT_RETRY_COUNT;
    int timeout() default DEFAULT_MIN_TIMEOUT;
}
