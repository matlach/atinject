package org.atinject.core.cache;

import org.jgroups.logging.CustomLogFactory;
import org.jgroups.logging.Log;

public class CustomJGroupsLoggerFactory implements CustomLogFactory {

	@Override
	public Log getLog(Class clazz) {
		return new SLF4JJGroupsLogger(clazz);
	}

	@Override
	public Log getLog(String category) {
		return new SLF4JJGroupsLogger(category);
	}

}
