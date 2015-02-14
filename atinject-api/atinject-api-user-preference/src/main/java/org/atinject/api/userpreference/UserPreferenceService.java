package org.atinject.api.userpreference;

import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.atinject.api.user.validation.ExistingUserId;
import org.atinject.api.userpreference.entity.UserPreferencesEntity;
import org.atinject.core.cache.DistributedCache;
import org.atinject.core.cdi.Named;
import org.atinject.core.tiers.Service;

@Service
public class UserPreferenceService {

    @Inject @Named("user-preferences") private DistributedCache<UUID, UserPreferencesEntity> userPreferenceCacheStore;
    
    public UserPreferencesEntity getUserPreferences(@NotNull UUID userId){
        return userPreferenceCacheStore.get(userId)
        		.orElseGet(() -> lockAndGetUserPrefererences(userId)
        		.orElse(addUserPreference(userId)));
    }

    protected Optional<UserPreferencesEntity> lockAndGetUserPrefererences(@NotNull UUID userId) {
    	userPreferenceCacheStore.lock(userId);
    	return userPreferenceCacheStore.get(userId);
    }
    
    protected UserPreferencesEntity addUserPreference(@NotNull @ExistingUserId UUID userId) {
        UserPreferencesEntity userPreferences = new UserPreferencesEntity()
            .setUserId(userId);
        userPreferenceCacheStore.put(userId, userPreferences);
        return userPreferences;
    }
    
    public void updateUserPreferences(UserPreferencesEntity userPreferences){
        userPreferenceCacheStore.put(userPreferences.getUserId(), userPreferences);
    }
}
