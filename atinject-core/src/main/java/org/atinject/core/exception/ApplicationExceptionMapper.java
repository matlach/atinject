package org.atinject.core.exception;

import org.atinject.core.tiers.exception.ExceptionMapper;

public class ApplicationExceptionMapper extends ExceptionMapper<ApplicationException> {

	@Override
	public String getFault(ApplicationException exception) {
		return exception.getExceptionCode();
	}

}
