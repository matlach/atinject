package org.atinject.core.startup;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import javax.enterprise.inject.spi.WithAnnotations;

public class StartupExtension implements Extension {
	
    private final Set<ProcessBean<?>> startupBeans = new LinkedHashSet<>();

    <X> void processBean(@Observes @WithAnnotations(Startup.class) ProcessBean<X> event) {
        startupBeans.add(event);
    }
    
    void afterDeploymentValidation(@Observes AfterDeploymentValidation event, BeanManager manager) {
    	startupBeans.stream()
    			.sorted((e1, e2) -> Integer.compare(
    					e2.getAnnotated().getAnnotation(Startup.class).priority(),
    					e1.getAnnotated().getAnnotation(Startup.class).priority()))
    			.forEach(
    					// the call to toString() is a cheat to force the bean to be initialized
    					(processBean) -> manager.getReference(
    							processBean.getBean(),
    							processBean.getBean().getBeanClass(),
    							manager.createCreationalContext(processBean.getBean())).toString());
    }
    
}
