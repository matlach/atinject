package org.atinject.api.user;

import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;

public class UserServiceIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return createDefaultArchive(UserServiceIT.class);
    }
    
    @Inject
    private UserService userService;
    
    private static UUID userId;
    
    @Test
    @InSequence(1)
    public void testAdd() {
        UserEntity user = userService.addUser("123");
        userId = user.getId();
    }
    
    @Test
    @InSequence(2)
    public void testGet() {
        Optional<UserEntity> user = userService.getUser(userId);
        Assert.assertTrue(user.isPresent());
    }
    
    @Test
    @InSequence(3)
    public void testUpdate() {
        UserEntity user = userService.getUser(userId).get(); 
        user.setName("456");
        userService.updateUser(user);
        
        user = userService.getUser(userId).get();
        Assert.assertEquals("456", user.getName());
    }
    
    @Test
    @InSequence(4)
    public void testRemove() {
        UserEntity user = userService.getUser(userId).get();
        userService.removeUser(user);
        
        user = userService.getUser(user.getId()).orElse(null);
        Assert.assertNull(user);
    }
}
