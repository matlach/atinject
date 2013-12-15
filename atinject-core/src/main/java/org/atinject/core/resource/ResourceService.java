package org.atinject.core.resource;

import java.util.List;

import org.atinject.core.resource.entity.Resource;
import org.atinject.core.resource.enumeration.Locales;
import org.atinject.core.tiers.Service;

public class ResourceService extends Service {

    public static final String DEFAULT_LOCALE = Locales.ENGLISH_UNITED_STATES;
    
    public void getResourceBundle(String resourceBundleId) {
        getResourceBundle(resourceBundleId, DEFAULT_LOCALE);
    }
    
    public List<Resource> getResourceBundle(String resourceBundleId, String localeId) {
        return null;
    }
    
    protected List<Resource> filterResourcesByBundleIdAndLocaleId(List<Resource> resources, String bundleId, String localeId) {
        return null;
    }
    
    protected List<Resource> filterResourcesByBundleId(List<Resource> resources, String bundleId) {
        return null;
    }
    
    protected List<Resource> filterResourcesByLocaleId(List<Resource> resources, String localeId) {
        return null;
    }
    
    public void exportFor3thParty(String resourceBundleId, String filename) {
        
    }
    
    public void exportForIntegration(String resourceBundleId, String localeId, String filename) {
        
    }
}
