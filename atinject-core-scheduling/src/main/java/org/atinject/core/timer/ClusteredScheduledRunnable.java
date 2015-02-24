package org.atinject.core.timer;

public class ClusteredScheduledRunnable implements Runnable {

    private Runnable delegate;
    
    public ClusteredScheduledRunnable(ScheduledRunnable runnable) {
        this.delegate =
            new TransactionalRunnableDecorator(
                new ZeroLockAcquisitionTimeoutRunnableDecorator(
                    runnable));
    }
    
    @Override
    public void run() {
        delegate.run();
    }

}
