package org.atinject.core.integration;

import org.atinject.core.cache.CustomJGroupsLoggerFactory;
import org.atinject.core.cdi.DefaultJavaSECDIProvider;



public class Bootstrap {

    public static void main(String[] args) throws Exception
    {
        // TODO create a custom logger for jgroups that will use slf4j
        // TODO add feature request / make pull request for jgroups
    	System.setProperty(org.jgroups.Global.CUSTOM_LOG_FACTORY, CustomJGroupsLoggerFactory.class.getName());
        
        // TODO create a wrapper class to provide default system properties
        // TODO this wrapper class should be instantiated by reflection using a well known system property like
        // -Dorg.atinject.bootstrap.custom.system.properties=org.atinject....
        System.setProperty("user.timezone", "GMT");

        Thread shutdownThread = new Thread(() -> {
        	try {
                System.in.read();
                System.exit(0);
            }
            catch (Exception e) {
                // swallow
            }
        });
        shutdownThread.setDaemon(true);
        shutdownThread.start();
        
        DefaultJavaSECDIProvider.initialize();
        
        // TODO inject here a "version service"
        // TODO version service should be able to read from MANIFEST.MF
        System.out.println("booting atinject core version 1");
    }
    
}
