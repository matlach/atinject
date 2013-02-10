package org.atinject.core.distexec;

import java.io.Serializable;

public class UserKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }
    
}
