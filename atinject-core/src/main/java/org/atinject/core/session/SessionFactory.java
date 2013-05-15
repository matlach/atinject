package org.atinject.core.session;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SessionFactory {

    public Session newSession(){
        return new SimpleSession();
    }
}
