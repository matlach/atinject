package org.atinject.core.logging;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;

public class LoggerProducer
{

    Logger logger = LoggerFactory.getLogger(LoggerProducer.class);
    
    @Produces
    public Logger logger(InjectionPoint ip)
    {
        if (ip == null){
            logger.warn("cannot inject logger, returning root logger");
            return LoggerFactory.getRootLogger();
        }
        if (ip.getBean() == null){
            logger.warn("cannot inject logger, returning root logger");
            return LoggerFactory.getRootLogger();
        }
        return LoggerFactory.getLogger(ip.getBean().getBeanClass());
    }
}
