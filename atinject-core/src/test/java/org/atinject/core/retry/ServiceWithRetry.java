package org.atinject.core.retry;

import org.atinject.core.concurrent.Retry;

public class ServiceWithRetry
{

    @Retry
    public void retry() {
        throw new RuntimeException();
    }
}
