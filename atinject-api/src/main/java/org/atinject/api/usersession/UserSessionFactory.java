package org.atinject.api.usersession;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Specializes;

import org.atinject.core.session.SessionFactory;

@Alternative @Specializes
@ApplicationScoped
public class UserSessionFactory extends SessionFactory {

    @Override
    public UserSession newSession(){
        return new UserSession();
    }
    
}
