package org.atinject.core.business;

public class ToLowerCaseExecutor implements BusinessExecutor<ToLowerCase, String> {

	@Override
	public void initialize(ToLowerCase businessAnnotation) {
		
	}

	@Override
	public String execute(String value) {
		return value.toLowerCase();
	}

}
