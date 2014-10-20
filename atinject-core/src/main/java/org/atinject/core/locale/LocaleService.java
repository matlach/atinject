package org.atinject.core.locale;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.atinject.core.cache.ReplicatedCache;
import org.atinject.core.locale.enumeration.AvailableLocales;
import org.atinject.core.tiers.Service;

@Service
public class LocaleService {

	@Inject
	private ReplicatedCache<String, Locale> supportedLocales;
	
	public Locale getSupportedLocale(String locale) {
		return null;
	}
	
	public List<Locale> getAllSupportedLocale() {
		return supportedLocales.values().stream().collect(Collectors.toList());
	}
	
	public void addSupportedLocale(String locale) {
		Locale availableLocale = AvailableLocales.fromLocale(locale);
		if (availableLocale == null) {
			throw new RuntimeException();
		}
		Locale supportedLocale = supportedLocales.get(locale);
		if (supportedLocale != null) {
			throw new RuntimeException();
		}
		supportedLocales.put(locale, availableLocale);////
	}
	
	public void removeSupportedLocale(String locale) {
		
	}
	
	public void getLocaleCandidate(String locale) {
		// language : ISO 639 alpha-2 or alpha-3
		// country : ISO 3166 alpha-2
		Locale l = new Locale("language", "country");
	}
}
