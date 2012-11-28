package org.atinject.core.user;

import javax.inject.Inject;

import org.atinject.integration.WeldRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

@RunWith(WeldRunner.class)
public class UserServiceTest
{

    @Inject
    private Logger logger;
    
    @Test
    public void testBla()
    {
        logger.info("test");
    }
}
