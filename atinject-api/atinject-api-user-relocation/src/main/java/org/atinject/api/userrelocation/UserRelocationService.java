package org.atinject.api.userrelocation;

import java.util.List;
import java.util.UUID;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.atinject.api.user.UserIdGenerator;
import org.atinject.api.user.UserService;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.userrelocation.event.UserRelocated;
import org.atinject.api.usertopology.UserTopologyService;
import org.atinject.core.affinity.AffineVersion4UUIDGenerator;
import org.atinject.core.tiers.Service;

@Service
public class UserRelocationService {

    @Inject
    private UserService userService;
    
    @Inject
    private UserTopologyService userTopologyService;
    
    @Inject
    private UserIdGenerator userIdGenerator;
    
    @Inject
    private Event<UserRelocated> userRelocatedEvent;
    
    @Inject
    private AffineVersion4UUIDGenerator affineVersion4UUIDGenerator;
    
    // TODO relocate to a server within the topology i.e. for a given machineId
    // TODO new userId should be locale to that topology
    // TODO validate the generated user id is unique
    public void relocateUser(UUID userId, String machineId) {
        UserEntity user = userService.getUser(userId).orElseThrow(() -> new NullPointerException());
        relocateUser(user, machineId);
    }
    
    public void relocateUser(UserEntity user, String machineId) {
        userService.removeUser(user);
        
        UUID userId = user.getId();
        // TODO check for clash
        UUID newUserId = userIdGenerator.generateUserId();
        user.setId(newUserId);
        userService.addUser(user);
        
        userRelocatedEvent.fire(new UserRelocated().setOldUserId(userId).setUser(user));
    }
    
    public void relocaleAllUser(String machineIdFrom, String machineIdTo) {
        List<UserEntity> users = userTopologyService.getAllUserByMachineId(machineIdFrom);
        for (UserEntity user : users) {
            relocateUser(user, machineIdTo);
        }
    }
    
}
