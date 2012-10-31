package org.atinject.core.distexec;

import java.io.Serializable;

public class UserKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uuid;

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    @Override
    public int hashCode()
    {
        return uuid.hashCode();
    }
    
}
