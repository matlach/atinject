package org.atinject.core.logging;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerProducer
{

    @Produces
    public Logger logger(InjectionPoint ip)
    {
        return LoggerFactory.getLogger(ip.getBean().getBeanClass());
    }
}
