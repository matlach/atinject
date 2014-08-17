package org.atinject.api.userpreference.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.atinject.core.entity.VersionableEntity;

public class UserPreferencesEntity extends VersionableEntity {

    private static final long serialVersionUID = 1L;

    private UUID userId;
    
    private Map<String, UserPreferenceEntity> preferences;

    public UserPreferencesEntity(){
        preferences = new HashMap<>();
    }
    
    public UUID getUserId() {
        return userId;
    }

    public UserPreferencesEntity setUserId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public Map<String, UserPreferenceEntity> getPreferences() {
        return preferences;
    }

	@Override
	public Object getId() {
		// TODO Auto-generated method stub
		return null;
	}
    
}
