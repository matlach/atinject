package org.atinject.core.jndi;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.spi.InitialContextFactory;

public class SimpleInitialContextFactory implements InitialContextFactory {

	@Override
	public Context getInitialContext(Hashtable<?,?> environment) {
		return new SimpleNamingContext();
	}

}
