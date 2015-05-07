package org.atinject.core.business;

public class EmptyStringToNullExecutor implements BusinessExecutor<EmptyStringToNull, String> {

	@Override
	public void initialize(EmptyStringToNull businessAnnotation) {
		
	}

	@Override
	public String execute(String value) {
		if ("".equals(value)) {
			return null;
		}
		return value;
	}

}
