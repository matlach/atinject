package org.atinject.core.marshallable;

import java.io.Serializable;

public abstract class MarshallableObject implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	// TODO override clone method using fast byte array technique ?
	// use netty byte buf ?
	// see if jboss marshaller could be used
	// jackson smile ? 
	@Override
	public MarshallableObject clone(){
	    
	    return null;
	}
}
