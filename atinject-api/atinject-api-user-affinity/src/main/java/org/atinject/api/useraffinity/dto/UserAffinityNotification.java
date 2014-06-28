package org.atinject.api.useraffinity.dto;

import org.atinject.core.websocket.dto.WebSocketNotification;

public class UserAffinityNotification extends WebSocketNotification
{
    private static final long serialVersionUID = 1L;

    private String url;
    
    public String getUrl()
    {
        return url;
    }

    public UserAffinityNotification setUrl(String url)
    {
        this.url = url;
        return this;
    }
    
}
