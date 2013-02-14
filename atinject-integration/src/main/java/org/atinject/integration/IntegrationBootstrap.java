package org.atinject.integration;

import org.atinject.core.cdi.Weld;


public class IntegrationBootstrap {

    public static void main(String[] args) throws Exception
    {
        System.setProperty("user.timezone", "GMT");

        Thread shutdownThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    System.in.read();
                    System.exit(0);
                }
                catch (Exception e)
                {
                    // swallow
                }
            }
        });
        shutdownThread.setDaemon(true);
        shutdownThread.start();

        final Weld weld = new Weld();
        weld.initialize();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                weld.shutdown();
            }
        }));
    }
	
}
