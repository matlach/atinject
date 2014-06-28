package org.atinject.core.logging;

import org.slf4j.Logger;

public final class LoggerFactory {
	
    private LoggerFactory() {
        
    }
    
    public static Logger getRootLogger() {
        return org.slf4j.LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    }
    
    public static Logger getLogger(Class<?> clazz) {
        return org.slf4j.LoggerFactory.getLogger(clazz);
    }
    
    public static Logger getLogger(String name) {
        return org.slf4j.LoggerFactory.getLogger(name);
    }
}
