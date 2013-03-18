package org.atinject.core.validation;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.util.AnnotationLiteral;

import org.apache.deltaspike.core.util.metadata.builder.AnnotatedTypeBuilder;
import org.atinject.core.tiers.CacheStore;
import org.atinject.core.tiers.Service;
import org.atinject.core.tiers.WebSocketService;


public class ValidateMethodInterceptorExtension implements Extension {

    <T> void processAnnotatedType(@Observes final ProcessAnnotatedType<T> pat) {
        class ValidateMethodLiteral extends AnnotationLiteral<ValidateMethod> {
            private static final long serialVersionUID = 1L;
        }

        if (WebSocketService.class.isAssignableFrom(pat.getAnnotatedType().getJavaClass()) ||
            Service.class.isAssignableFrom(pat.getAnnotatedType().getJavaClass()) ||
            CacheStore.class.isAssignableFrom(pat.getAnnotatedType().getJavaClass())){
        
            final AnnotatedTypeBuilder<T> builder = new AnnotatedTypeBuilder<T>()
                    .readFromType(pat.getAnnotatedType(), true)
                    .addToClass(new ValidateMethodLiteral());
            pat.setAnnotatedType(builder.create());
        }
    }

}
