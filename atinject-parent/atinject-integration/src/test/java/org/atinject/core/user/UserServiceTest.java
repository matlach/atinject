package org.atinject.core.user;

import javax.inject.Inject;

import junit.framework.Assert;

import org.atinject.api.user.UserService;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.integration.WeldRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

@RunWith(WeldRunner.class)
public class UserServiceTest
{

    @Inject
    private Logger logger;

    @Inject
    private UserService userService;
    
    @Test
    public void testAddGetUpdateGetRemoveUser()
    {
        logger.info("add");
        UserEntity user = userService.addUser("123", "***");
        Assert.assertNotNull(user);

        logger.info("get");
        user = userService.getUser(user.getId());
        Assert.assertNotNull(user);

        logger.info("update");
        user.setName("456");
        userService.updateUser(user);
        
        logger.info("get updated");
        user = userService.getUser(user.getId());
        
        logger.info("remove");
        userService.removeUser(user);
        
        user = userService.getUser(user.getId());
        
        Assert.assertNull(user);
    }
}
