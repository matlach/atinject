package org.atinject.api.user.adapter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.user.UserDTOFactory;
import org.atinject.api.user.UserEntityFactory;
import org.atinject.api.user.dto.User;
import org.atinject.api.user.entity.UserEntity;

@ApplicationScoped
public class UserAdapter {

    @Inject
    private UserDTOFactory userDTOFactory;
    
    @Inject
    private UserEntityFactory userEntityFactory;
    
    public User userEntityToUser(UserEntity entity) {
        if (entity == null){
            return null;
        }
        return userDTOFactory.newUser()
            .setId(entity.getId())
            .setName(entity.getName());
    }
}
