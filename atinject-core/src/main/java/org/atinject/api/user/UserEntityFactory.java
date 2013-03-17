package org.atinject.api.user;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.core.cdi.CDI;
import org.atinject.core.entity.EntityFactory;

@ApplicationScoped
public class UserEntityFactory extends EntityFactory
{

    public UserEntity newUserEntity(){
        return CDI.select(UserEntity.class).get();
    }
}
