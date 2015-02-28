package org.atinject.core.profiling;

import java.lang.annotation.Annotation;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;

import org.atinject.core.tiers.Service;
import org.atinject.core.tiers.WebSocketService;
import org.jboss.weld.util.annotated.AnnotatedTypeWrapper;

public class ProfileExtension implements Extension {

    <T> void processAnnotatedType(@Observes @WithAnnotations({WebSocketService.class, Service.class}) ProcessAnnotatedType<T> event) {
    	
    	Annotation profileAnnotation = new Annotation() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return Profile.class;
			}
		};
		
		event.setAnnotatedType(new AnnotatedTypeWrapper<>(event.getAnnotatedType(), profileAnnotation));
     }
    
}
