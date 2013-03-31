package org.atinject.api.session;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SessionFactory {

    public Session newSession(){
        return new Session();
    }
}
