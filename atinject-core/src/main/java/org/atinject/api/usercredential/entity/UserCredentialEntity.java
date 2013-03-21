package org.atinject.api.usercredential.entity;

import org.atinject.core.entity.VersionableEntity;

public class UserCredentialEntity extends VersionableEntity {

    private static final long serialVersionUID = 1L;
    
    private String username;
    private String password;
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

    public String getPassword() {
        return password;
    }

    public UserCredentialEntity setPassword(String password) {
        this.password = password;
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
