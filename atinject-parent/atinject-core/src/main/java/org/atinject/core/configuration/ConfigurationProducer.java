package org.atinject.core.configuration;

import java.lang.reflect.Member;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

public class ConfigurationProducer {

	
	org.apache.commons.configuration.Configuration configuration;
	
	@Produces
	@Configuration
	public String stringConfiguration(InjectionPoint injectionPoint){
		//return configuration.getString(getKey(injectionPoint));
		return getKey(injectionPoint);
	}
	
	private String getKey(InjectionPoint injectionPoint){
		return getKey(injectionPoint.getBean().getBeanClass().getPackage(),
				injectionPoint.getBean().getBeanClass(),
				injectionPoint.getMember());
	}
	
	private String getKey(Package p, Class<?> c, Member m)
	{
		return new StringBuilder()
			.append(applyPackageNamingConvention(p))
			.append(".")
			.append(applyBeanClassNamingConvention(c))
			.append(".")
			.append(applyMemberNamingConvention(m))
			.toString();
	}
	
	public String applyPackageNamingConvention(Package p)
	{
		return p.getName().toLowerCase();
	}
	
    public String applyBeanClassNamingConvention(Class<?> c)
    {
    	return applyNamingConvention(c.getSimpleName());
    }
    
    public String applyMemberNamingConvention(Member m){
    	return applyNamingConvention(m.getName());
    }
    
    private String applyNamingConvention(String s)
    {
    	StringBuilder name = new StringBuilder();
        for (char ch : s.toCharArray()){
        	if (Character.isUpperCase(ch)){
        		if (name.length() > 0)
        		{
        			name.append(".");
        		}
        		name.append(Character.toLowerCase(ch));
        	}
        	else
        	{
        		name.append(ch);
        	}
        }
        return name.toString();
    }
}
