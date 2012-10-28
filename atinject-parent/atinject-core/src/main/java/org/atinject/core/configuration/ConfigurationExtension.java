package org.atinject.core.configuration;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessInjectionTarget;


public class ConfigurationExtension implements Extension{

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
