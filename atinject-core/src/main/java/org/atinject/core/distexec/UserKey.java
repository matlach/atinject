package org.atinject.core.distexec;

import java.io.Serializable;

public class UserKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    public String getId()
    {
        return id;
    }

    public UserKey setId(String id)
    {
        this.id = id;
        return this;
    }

    @Override
    public int hashCode()
    {
        // TODO hash code need to be cached
        return id.hashCode();
    }
    
}
