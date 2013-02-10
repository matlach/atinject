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

    public void setChannelId(Integer channelId)
    {
        this.channelId = channelId;
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getMachineId()
    {
        return machineId;
    }

    public void setMachineId(String machineId)
    {
        this.machineId = machineId;
    }

    public String getRackId()
    {
        return rackId;
    }

    public void setRackId(String rackId)
    {
        this.rackId = rackId;
    }

    public String getSiteId()
    {
        return siteId;
    }

    public void setSiteId(String siteId)
    {
        this.siteId = siteId;
    }
    
}