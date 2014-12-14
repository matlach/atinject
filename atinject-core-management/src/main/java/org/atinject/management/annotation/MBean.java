package org.atinject.management.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author morten.hattesen@gmail.com
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface MBean {
    /**
     * @return the ObjectName with which the MBean should be registered with the MBean server.<P>
     * Refer to {@link javax.management.ObjectName} for details of objectname syntax
     * Sample object names:<br>
     * org.softee.management:name=MyBean,type=org.softee.management.ProcessingMonitor
     * org.softee.management:application=ESB,name=MyBean,type=org.softee.management.ProcessingMonitor
     */
    String objectName() default "";
}