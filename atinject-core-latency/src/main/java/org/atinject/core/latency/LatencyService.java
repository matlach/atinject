package org.atinject.core.latency;

import org.atinject.core.tiers.Service;

@Service
public class LatencyService {

    public long getTime() {
        return System.currentTimeMillis();
    }
}
