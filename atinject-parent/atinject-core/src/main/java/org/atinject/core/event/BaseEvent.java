package org.atinject.core.event;

import java.io.Serializable;

public abstract class BaseEvent implements Serializable
{

    private static final long serialVersionUID = 1L;

    // if not distributed i.e. not serialized, origin local is preserved, else false.
    private transient boolean originLocal = true;

    public boolean isOriginLocal()
    {
        return originLocal;
    }

    public void setOriginLocal(boolean originLocal)
    {
        this.originLocal = originLocal;
    }
    
}
