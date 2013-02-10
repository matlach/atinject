package org.atinject.api.user.dto;

import org.atinject.core.websocket.dto.BaseWebSocketNotification;

public class UserAffinityNotification extends BaseWebSocketNotification
{
    private static final long serialVersionUID = 1L;

    private String url;
    
    public UserAffinityNotification(){
        super();
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }
    
}
