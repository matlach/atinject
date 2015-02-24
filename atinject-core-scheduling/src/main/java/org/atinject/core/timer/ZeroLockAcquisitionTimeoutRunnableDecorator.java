package org.atinject.core.timer;

public class ZeroLockAcquisitionTimeoutRunnableDecorator implements Runnable {

    private Runnable delegate;
    
    public ZeroLockAcquisitionTimeoutRunnableDecorator(Runnable runnable/*String cacheName, String key*/) {
        this.delegate = runnable;
    }
    
    @Override
    public void run() {
        // TODO
        // Cache cache = CDI.select(cache).get();
        // if (!cache.zerotimelockacquisition.failSilently.lock(key)) {
        //     return;
        // }
        delegate.run();
    }

}
