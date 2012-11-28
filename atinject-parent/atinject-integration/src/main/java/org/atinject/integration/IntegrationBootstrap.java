package org.atinject.integration;

import java.io.IOException;

import org.atinject.core.cdi.Weld;


public class IntegrationBootstrap {

    public static void main(String[] args) throws InterruptedException, IOException
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
                }
                catch (Exception e)
                {
                    // swallow
                }
                System.exit(0);
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
