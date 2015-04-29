package org.atinject.core.business;

import java.lang.annotation.Annotation;

public interface BusinessExecutor<A extends Annotation, T> {

	void initialize(A businessAnnotation);
	
	T execute(T value);
}
