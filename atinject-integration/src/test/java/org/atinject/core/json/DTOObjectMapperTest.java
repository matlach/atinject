package org.atinject.core.json;

import javax.inject.Inject;

import org.atinject.api.session.dto.SessionOpenedNotification;
import org.atinject.core.dto.DTOObjectMapper;
import org.atinject.integration.WeldRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

@RunWith(WeldRunner.class)
public class DTOObjectMapperTest 
{

    @Inject
    private Logger logger;
    
    @Inject
    private DTOObjectMapper dtoObjectMapper;
    
    @Test
    public void serializeSessionOpenedNotification(){
        
        SessionOpenedNotification newNotification = new SessionOpenedNotification();
        newNotification.setSessionId("123");
        
        String json = dtoObjectMapper.writeValueAsString(newNotification);
        
        SessionOpenedNotification notification = dtoObjectMapper.readValue(json);
        
        Assert.assertEquals(newNotification.getSessionId(), notification.getSessionId());
        
        logger.info(json);
    }
    
}
