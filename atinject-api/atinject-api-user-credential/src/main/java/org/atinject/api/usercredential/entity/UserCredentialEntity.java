package org.atinject.api.usercredential.entity;

import java.util.UUID;

import org.atinject.core.entity.VersionableEntity;

public class UserCredentialEntity extends VersionableEntity {

    private static final long serialVersionUID = 1L;
    
    private String username;
    private String salt;
    private String hash;
    private UUID userId;
    
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
    
    public String getSalt() {
        return salt;
    }

    public UserCredentialEntity setSalt(String salt) {
        this.salt = salt;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public UserCredentialEntity setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public UUID getUserId() {
        return userId;
    }

    public UserCredentialEntity setUserId(UUID userId) {
        this.userId = userId;
        return this;
    }

}
