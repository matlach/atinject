package org.atinject.core.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.atinject.core.cache.ReplicatedCache;
import org.atinject.core.locale.LocaleService;
import org.atinject.core.locale.enumeration.AvailableLocales;
import org.atinject.core.resource.entity.ResourceBundleEntity;
import org.atinject.core.resource.entity.ResourceEntity;
import org.atinject.core.resource.entity.ResourceStringEntity;
import org.atinject.core.tiers.Service;

public class ResourceService extends Service {

	@Inject
	private ReplicatedCache<String, ResourceBundleEntity> resourceBundles;
	
    @Inject
    private ReplicatedCache<String, ResourceEntity> resources;
    
    @Inject
    private ReplicatedCache<String, ResourceStringEntity> resourceStrings;
    
    @Inject
    private LocaleService localeService;
    
    public static final String DEFAULT_LOCALE = AvailableLocales.ENGLISH_UNITED_STATES;
    
    public List<Locale> getAllLocaleCandidate(Locale locale) {
    	List<Locale> candidateLocales = new ArrayList<>();
		
		List<Locale> supportedLocales = localeService.getAllSupportedLocale();
		if (!"".equals(locale.getLanguage()) && !"".equals(locale.getCountry())) {
			if (supportedLocales.contains(locale)) {
				candidateLocales.add(locale);
			}
		}
		if (!"".equals(locale.getLanguage())) {
			Locale languageOnly = new Locale(locale.getLanguage());
			if (supportedLocales.contains(languageOnly)) {
				candidateLocales.add(languageOnly);
			}
		}
		return candidateLocales;
    }
    
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
    
//  TODO rely on standard mechanism ?
//  
//  ResourceBundle resourceBundle = ResourceBundle.getBundle("default", new Locale("en", "US"));
//    
//  public class ExtendedResourceBundleControlProvider implements ResourceBundleControlProvider {
//		@Override
//		public Control getControl(String baseName) {
//			return new ExtendedResourceBundleControl();
//		}
//  }
//  
//	public class ExtendedResourceBundleControl extends ResourceBundle.Control {
//
//		@Override
//		public List<Locale> getCandidateLocales(String baseName, Locale locale) {
//			if (baseName == null) {
//				throw new NullPointerException();
//			}
//			// if language + country is available
//			// if language + country is supported, add language + country
//			// if language is supported, add language
//			// finally, either case, add default language and ROOT
//			
//			List<Locale> candidateLocales = getAllLocaleCandidate(locale);
//			candidateLocales.add(Locale.ROOT);
//			return Collections.unmodifiableList(candidateLocales);
//		}
//
//	}
//  
//  public class ExtendedResourceBundle extends ResourceBundle {
//  	
//  	@Override
//  	protected Object handleGetObject(String key) {
//  		String bundleName = getBaseBundleName();
//  		String locale = getLocale().toString();
//  		String resourceId = resources.get(key).toString();
//  		return resourceStrings.get(bundleName + "/" + key + "/" + locale);
//  	}
//
//  	@Override
//  	public Enumeration<String> getKeys() {
//  		return Collections.enumeration(resources.keySet()); //+ parent
//  	}
//
//  }
}
