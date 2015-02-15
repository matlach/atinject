package org.atinject.core.tiers;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Stereotype;

import org.atinject.core.profiling.Profile;
import org.atinject.core.thread.ThreadTracker;
import org.atinject.core.tiers.exception.HandleServiceException;
import org.atinject.core.transaction.Transactional;
import org.atinject.core.validation.Validate;

@ApplicationScoped
@Stereotype
@HandleServiceException
@Profile
@ThreadTracker
@Transactional
@Validate
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface Service {


}
