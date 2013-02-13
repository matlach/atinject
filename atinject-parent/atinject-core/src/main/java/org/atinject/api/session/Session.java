package org.atinject.api.session;

import org.atinject.core.marshallable.MarshallableObject;

public class Session extends MarshallableObject
{
    private static final long serialVersionUID = 1L;
    
    private Integer channelId;
    
    private String sessionId;
    
    private String machineId;
    
    private String rackId;
    
    private String siteId;
    
    private String userId;
    
    public Session(){
        super();
    }

    public Integer getChannelId()
    {
        return channelId;
    }

    public Session setChannelId(Integer channelId)
    {
        this.channelId = channelId;
        return this;
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public Session setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
        return this;
    }

    public String getUserId()
    {
        return userId;
    }

    public Session setUserId(String userId)
    {
        this.userId = userId;
        return this;
    }

    public String getMachineId()
    {
        return machineId;
    }

    public Session setMachineId(String machineId)
    {
        this.machineId = machineId;
        return this;
    }

    public String getRackId()
    {
        return rackId;
    }

    public Session setRackId(String rackId)
    {
        this.rackId = rackId;
        return this;
    }

    public String getSiteId()
    {
        return siteId;
    }

    public Session setSiteId(String siteId)
    {
        this.siteId = siteId;
        return this;
    }
    
}