package org.atinject.core.configuration;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.atinject.core.cdi.Named;
import org.atinject.core.tiers.Service;

@Service
public class ConfigurationService {

	private Map<String, String> configurations; 
	
	@PostConstruct
	public void initialize() {
		// find all resources matching *configuration.properties
	}
	
	@Produces @Named
	public String stringConfiguration(InjectionPoint ip) {
		String key = ip.getAnnotated().getAnnotation(Named.class).value();
		return configurations.get(key);
	}
	
	@Produces @Named
	public boolean booleanConfiguration(InjectionPoint ip) {
		return Boolean.parseBoolean(stringConfiguration(ip));
	}
	
	@Produces @Named
	public int intConfiguration(InjectionPoint ip) {
		return Integer.parseInt(stringConfiguration(ip));
	}
	
	@Produces @Named
	public long longConfiguration(InjectionPoint ip) {
		return Long.parseLong(stringConfiguration(ip));
	}
	
	@Produces @Named
	public float floatConfiguration(InjectionPoint ip) {
		return Float.parseFloat(stringConfiguration(ip));
	}
	
	@Produces @Named
	public double doubleConfiguration(InjectionPoint ip) {
		return Double.parseDouble(stringConfiguration(ip));
	}
}
