package org.atinject.core.configuration;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessInjectionTarget;

import org.apache.commons.configuration.PropertiesConfiguration;


public class ConfigurationExtension implements Extension{

    private static final String PROPERTIES_CONFIGURATION = "org.atinject.";
    
    private PropertiesConfiguration configuration;
    
    public ConfigurationExtension()
    {
        System.setProperty(PROPERTIES_CONFIGURATION, "/Users/mathieu/Desktop/git/atinject-parent/atinject-integration/src/main/resources/configuration.properties");
        
        String propertiesFilePath = System.getProperty(PROPERTIES_CONFIGURATION);
        if (propertiesFilePath == null)
        {
            throw new ExceptionInInitializerError("-D" + PROPERTIES_CONFIGURATION + " is not specified");
        }
        
        File propertiesFile = new File(propertiesFilePath);
        if (! propertiesFile.exists())
        {
            throw new ExceptionInInitializerError("file '" + propertiesFilePath + "' does not exists");
        }
        
        try
        {
            configuration = new PropertiesConfiguration(propertiesFile);
        }
        catch (Exception e)
        {
            throw new ExceptionInInitializerError(e);
        }
    }
    
    public PropertiesConfiguration getConfiguration()
    {
        return configuration;
    }
    
	public void onProcessInjectionTarget(@Observes ProcessInjectionTarget<?> pit)
	{
		Set<InjectionPoint> injectionPoints = pit.getInjectionTarget().getInjectionPoints();
		for (InjectionPoint injectionPoint : injectionPoints)
		{			
			Set<Annotation> qualifiers = injectionPoint.getQualifiers();
			for (Annotation qualifier : qualifiers)
			{
				if (qualifier.annotationType().equals(Configuration.class))
				{
					System.out.println(injectionPoint.getBean().getBeanClass().getCanonicalName() + "::" + injectionPoint.getMember());
				}
			}
		}
	}
}
