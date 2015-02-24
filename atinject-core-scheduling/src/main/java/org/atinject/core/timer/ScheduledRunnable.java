package org.atinject.core.timer;

import org.atinject.core.cdi.CDI;
import org.atinject.core.concurrent.ScheduledService;

public class ScheduledRunnable implements Runnable {

    public ScheduledRunnable(/*, Timer, etc. */) {
    }
    
    @Override
    public void run() {
        ScheduledService scheduledService = CDI.select(ScheduledService.class).get();
        // TODO perform timer stuff
        // try {
        //     method.invoke();
        // }
        // finally {
        //     timer.update();
        //     scheduledService.schedule(...);
        // }
    }

}
