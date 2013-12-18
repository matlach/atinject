package org.atinject.api.userrelocation;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.atinject.api.user.UserIdGenerator;
import org.atinject.api.user.UserService;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.userrelocation.event.UserRelocated;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class UserRelocationService extends Service {

    @Inject
    private UserService userService;
    
    @Inject
    private UserIdGenerator userIdGenerator;
    
    @Inject
    private Event<UserRelocated> userRelocatedEvent;
    
    // TODO relocate to a server within the topology i.e. for a given machineId
    // TODO new userId should be locale to that topology
    // TODO validate the generated user id is unique
    public void relocateUser(UUID userId) {
        UserEntity user = userService.getUser(userId);
        userService.removeUser(userId);
        
        UUID newUserId = userIdGenerator.generateUserId();
        user.setId(newUserId);
        userService.addUser(user);
        
        userRelocatedEvent.fire(new UserRelocated().setOldUserId(userId).setUser(user));
    }
    
    
    
}
