package org.atinject.core.cdi;

import org.jboss.weld.environment.se.Weld;

public final class DefaultJavaSECDIProvider {

    private DefaultJavaSECDIProvider() {
        // utility class
    }
    
    private static Weld weld;
    
    public static void initialize() {
        weld = new Weld();
        weld.initialize();
        
        Runtime.getRuntime()
        	.addShutdownHook(
        		new Thread(() -> weld.shutdown()));
    }
    
}
