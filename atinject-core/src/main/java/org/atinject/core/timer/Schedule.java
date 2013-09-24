package org.atinject.core.timer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * default schedule is 00:00:00 each day, forever, and not clustered
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Schedule
{
    int[] seconds() default {0};
    int[] minutes() default {0};
    int[] hours() default {0};
    int[] daysOfWeek() default {-1};
    int[] daysOfMonth() default {-1};
    int[] months() default {-1};
    int years() default -1;
    
    boolean clustered() default false;
}
