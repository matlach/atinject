package org.atinject.integration;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMinimalDeployment extends JavaDeployment {

	private static Logger logger = LoggerFactory.getLogger(DefaultMinimalDeployment.class);
	
	public DefaultMinimalDeployment(Class<? extends IntegrationTest> integrationTestClass) {
		super(integrationTestClass);
	}

	@Override
	public JavaArchive getArchive() {
		logger.info(archive.toString(true));
		return archive;
	}
	
}
