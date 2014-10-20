package org.atinject.api.userpreference;

import java.util.UUID;

import javax.inject.Inject;

import org.atinject.api.userpreference.entity.UserPreferencesEntity;
import org.atinject.core.cache.DistributedCache;
import org.atinject.core.cdi.Named;
import org.atinject.core.tiers.Service;

@Service
public class UserPreferenceService {

    @Inject @Named("user-preferences") private DistributedCache<UUID, UserPreferencesEntity> userPreferenceCacheStore;
    
    public UserPreferencesEntity getUserPreferences(UUID userId){
        UserPreferencesEntity userPreferences = userPreferenceCacheStore.get(userId);
        if (userPreferences != null){
            return userPreferences;
        }
        
        userPreferenceCacheStore.lock(userId);
        userPreferences = userPreferenceCacheStore.get(userId);
        if (userPreferences != null){
            return userPreferences;
        }
        
        userPreferences = new UserPreferencesEntity()
            .setUserId(userId);
        userPreferenceCacheStore.put(userId, userPreferences);
        
        return userPreferences;
    }
    
    public void updateUserPreferences(UserPreferencesEntity userPreferences){
        userPreferenceCacheStore.put(userPreferences.getUserId(), userPreferences);
    }
}
