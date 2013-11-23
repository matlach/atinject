package org.atinject.api.registration;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DefaultGuestUsernamePasswordGenerator implements GuestUsernamePasswordGenerator {

    @Override
    public String generateUsername() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String generatePassword() {
        return UUID.randomUUID().toString();
    }

}
