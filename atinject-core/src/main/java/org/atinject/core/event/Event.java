package org.atinject.core.event;

import java.io.Serializable;

public abstract class Event implements Serializable
{

    private static final long serialVersionUID = 1L;

    // if not distributed i.e. not serialized, origin local is preserved, else false.
    private transient boolean originLocal;

    public boolean isOriginLocal()
    {
        return originLocal;
    }

    public Event setOriginLocal(boolean originLocal)
    {
        this.originLocal = originLocal;
        return this;
    }
    
}
