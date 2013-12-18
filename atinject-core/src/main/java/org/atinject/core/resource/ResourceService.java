package org.atinject.core.resource;

import java.util.List;

import javax.inject.Inject;

import org.atinject.core.cache.ReplicatedCache;
import org.atinject.core.locale.enumeration.Locales;
import org.atinject.core.resource.entity.ResourceEntity;
import org.atinject.core.resource.entity.ResourceMetadataEntity;
import org.atinject.core.tiers.Service;

public class ResourceService extends Service {

    @Inject
    private ReplicatedCache<String, ResourceEntity> resources;
    
    @Inject
    private ReplicatedCache<String, ResourceMetadataEntity> resourceMetadatas;
    
    public static final String DEFAULT_LOCALE = Locales.ENGLISH_UNITED_STATES;
    
    public void getResourceBundle(String resourceBundleId) {
        getResourceBundle(resourceBundleId, DEFAULT_LOCALE);
    }
    
    public List<ResourceEntity> getResourceBundle(String resourceBundleId, String localeId) {
        return null;
    }
    
    protected List<ResourceEntity> filterResourcesByBundleIdAndLocaleId(List<ResourceEntity> resources, String bundleId, String localeId) {
        return null;
    }
    
    protected List<ResourceEntity> filterResourcesByBundleId(List<ResourceEntity> resources, String bundleId) {
        return null;
    }
    
    protected List<ResourceEntity> filterResourcesByLocaleId(List<ResourceEntity> resources, String localeId) {
        return null;
    }
    
    public void exportFor3thParty(String resourceBundleId, String filename) {
        
    }
    
    public void exportForIntegration(String resourceBundleId, String localeId, String filename) {
        
    }
}
