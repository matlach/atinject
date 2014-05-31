package org.atinject.core.tiers.exception;


public abstract class ExceptionMapper<T extends Exception> {

	public abstract String getFault(T exception);
}
