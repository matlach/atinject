package org.atinject.api.user;

import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.core.cache.CacheExtension;
import org.atinject.core.transaction.InMemoryTransactionServices;
import org.atinject.integration.ArquillianIT;
import org.atinject.integration.DefaultDeployment;
import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

public class UserServiceIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
    	return new DefaultDeployment(ArquillianIT.class)
			.appendEmptyBeansXml()
			.appendJavaxEnterpriseInjectSpiExtension(CacheExtension.class)
			.appendOrgJBossWeldBootstrapApiService(InMemoryTransactionServices.class)
			.appendResource("arquillian-logback.xml", "logback.xml")
			.appendResource("arquillian-jgroups.xml", "jgroups.xml")
			.getArchive();
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
        Assertions.assertThat(user).isPresent();
    }
    
    @Test
    @InSequence(3)
    public void testUpdate() {
        UserEntity user = userService.getUser(userId).get(); 
        user.setName("456");
        userService.updateUser(user);
        
        user = userService.getUser(userId).get();
        Assertions.assertThat(user.getName()).isEqualTo("456");
    }
    
    @Test
    @InSequence(4)
    public void testRemove() {
        UserEntity user = userService.getUser(userId).get();
        userService.removeUser(user);
        
        user = userService.getUser(user.getId()).orElse(null);
        Assertions.assertThat(user).isNull();
    }
}
