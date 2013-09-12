package org.atinject.core.latency;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.core.tiers.Service;

@ApplicationScoped
public class LatencyService extends Service {

    public long getTime()
    {
        return System.currentTimeMillis();
    }
}
