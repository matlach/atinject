package org.atinject.integration;

import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filter;
import org.jboss.shrinkwrap.api.Filters;

public class DefaultIntegrationTestDeploymentExcludeFilter {

	private DefaultIntegrationTestDeploymentExcludeFilter() {
		
	}
	
	public static Filter<ArchivePath> get() {
		return Filters.exclude(".*(Dummy|Mock|Deployment|IT|Test|" + DefaultIntegrationTestDeploymentExcludeFilter.class.getSimpleName() + ")\\.class.*");
	}
}
