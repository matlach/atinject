package org.atinject.api.user.adapter;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.api.user.dto.User;
import org.atinject.api.user.entity.UserEntity;

@ApplicationScoped
public class UserAdapterImpl implements UserAdapter
{

    @Override
    public User userEntityToUser(UserEntity entity)
    {
        User dto = new User();
        return dto;
    }
}
