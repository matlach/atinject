package org.atinject.core.marshallable;

import java.io.Serializable;

public abstract class MarshallableObject implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @Override
    public MarshallableObject clone(){
        return MarshallableObjectCloner.cloneMarshallableObject(this);
    }
}
