package org.atinject.api.facebook;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.core.concurrent.Retry;

@ApplicationScoped
public class FacebookServiceImpl {

    @Retry
    public Object callGraphAPI() {
        throw new RuntimeException();
    }
}
