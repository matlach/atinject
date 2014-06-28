package org.atinject.api.registration;

import java.util.UUID;

import org.atinject.api.user.entity.UserEntity;

public interface RegistrationService {

    UserEntity registerAsGuest(String username, String password);
    
    UserEntity register(UUID userId, String username, String password);
}
