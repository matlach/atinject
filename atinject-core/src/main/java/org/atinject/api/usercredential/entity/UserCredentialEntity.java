package org.atinject.api.usercredential.entity;

import org.atinject.core.entity.VersionableEntity;

public class UserCredentialEntity extends VersionableEntity {

    private static final long serialVersionUID = 1L;
    
    private String username;
    private String passwordHash;
    private String userId;
    
    public UserCredentialEntity(){
        super();
    }

    public String getUsername() {
        return username;
    }

    public UserCredentialEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserCredentialEntity setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public UserCredentialEntity setUserId(String userId) {
        this.userId = userId;
        return this;
    }

}
