package org.atinject.api.userpreference;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.userpreference.entity.UserPreferencesEntity;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class UserPreferenceService extends Service {

    @Inject private UserPreferenceCacheStore userPreferenceCacheStore;
    
    public UserPreferencesEntity getUserPreferences(String userId){
        UserPreferencesEntity userPreferences = userPreferenceCacheStore.getUserPreferences(userId);
        if (userPreferences != null){
            return userPreferences;
        }
        
        userPreferenceCacheStore.lockUserPreferences(userId);
        userPreferences = userPreferenceCacheStore.getUserPreferences(userId);
        if (userPreferences != null){
            return userPreferences;
        }
        
        userPreferences = new UserPreferencesEntity()
            .setUserId(userId);
        userPreferenceCacheStore.putUserPreferences(userPreferences);
        
        return userPreferences;
    }
    
    public void updateUserPreferences(UserPreferencesEntity userPreferences){
        userPreferenceCacheStore.putUserPreferences(userPreferences);
    }
}
