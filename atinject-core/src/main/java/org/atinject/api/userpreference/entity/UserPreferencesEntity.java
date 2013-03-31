package org.atinject.api.userpreference.entity;

import java.util.HashMap;
import java.util.Map;

import org.atinject.core.entity.VersionableEntity;

public class UserPreferencesEntity extends VersionableEntity {

    private static final long serialVersionUID = 1L;

    private String userId;
    
    private Map<String, UserPreferenceEntity> preferences;

    public UserPreferencesEntity(){
        preferences = new HashMap<>();
    }
    
    public String getUserId() {
        return userId;
    }

    public UserPreferencesEntity setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public Map<String, UserPreferenceEntity> getPreferences() {
        return preferences;
    }
    
}
