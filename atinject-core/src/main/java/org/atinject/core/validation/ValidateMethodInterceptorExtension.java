package org.atinject.core.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Singleton;

public class ValidateMethodInterceptorExtension implements Extension {

    <T> void ProcessAnnotatedType(@Observes final ProcessAnnotatedType<T> pat) {
        
        final AnnotatedType<T> type = pat.getAnnotatedType();
        if (type.isAnnotationPresent(ApplicationScoped.class) || type.isAnnotationPresent(Singleton.class)) {
            AnnotatedType<T> wrappedType = new AnnotatedType<T>() {

                @Override
                public Type getBaseType() {
                    return type.getBaseType();
                }

                @Override
                public Set<Type> getTypeClosure() {
                    return type.getTypeClosure();
                }

                @Override
                public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
                    if (annotationType.equals(ValidateMethod.class)){
                        class ValidateMethodLiteral extends AnnotationLiteral<ValidateMethod> {
                            private static final long serialVersionUID = 1L;
                        }
                        return (T) new ValidateMethodLiteral();
                    }
                    return type.getAnnotation(annotationType);
                }

                @Override
                public Set<Annotation> getAnnotations() {
                    return type.getAnnotations();
                }

                @Override
                public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
                    if (annotationType.equals(ValidateMethod.class)){
                        return true;
                    }
                    return type.isAnnotationPresent(annotationType);
                }

                @Override
                public Class<T> getJavaClass() {
                    return type.getJavaClass();
                }

                @Override
                public Set<AnnotatedConstructor<T>> getConstructors() {
                    return type.getConstructors();
                }

                @Override
                public Set<AnnotatedMethod<? super T>> getMethods() {
                    return type.getMethods();
                }

                @Override
                public Set<AnnotatedField<? super T>> getFields() {
                    return type.getFields();
                }
            };
            pat.setAnnotatedType(wrappedType);
        }

    }

}
