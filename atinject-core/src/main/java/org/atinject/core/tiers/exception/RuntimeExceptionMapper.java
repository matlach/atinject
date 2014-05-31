package org.atinject.core.tiers.exception;


public class RuntimeExceptionMapper extends ExceptionMapper<RuntimeException> {

	@Override
	public String getFault(RuntimeException exception) {
		return exception.getMessage();
	}

}
