package org.atinject.integration;


public class DefaultDeployment extends DefaultMinimalDeployment {

	public DefaultDeployment(Class<? extends IntegrationTest> integrationTestClass) {
		super(integrationTestClass);
		appendPackage(DefaultIntegrationTestDeploymentExcludeFilter.get(), "org.atinject");
	}
	
}
