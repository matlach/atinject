package org.atinject.api.user.adapter;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.api.user.dto.User;
import org.atinject.api.user.entity.UserEntity;

@ApplicationScoped
public class UserAdapter
{

    public User userEntityToUser(UserEntity entity)
    {
        return new User()
            .setId(entity.getId())
            .setName(entity.getName())
            .setPassword(entity.getPassword());
    }
}
