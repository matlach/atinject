package org.atinject.api.analytic;

import java.util.Date;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.atinject.api.authentication.event.UserLoggedIn;
import org.atinject.core.affinity.LocalRandomUUIDGenerator;
import org.atinject.core.cache.DistributedCache;
import org.atinject.core.timer.Schedule;

@ApplicationScoped
public class TimeLoggedAnalyticService {

    @Inject
    LocalRandomUUIDGenerator idGenerator;

    // distributed cache to store raw data
    // elements will expire each day as they are crunched each day
    DistributedCache raw;

    // distributed cache to store crunched data
    DistributedCache crunched;

    public void onUserLogged(@Observes UserLoggedIn event) {
        // generate local event id
        UUID key = idGenerator.getKey();

        long t = System.currentTimeMillis();
        raw.put(key, t);
    }

    @Schedule(clustered = true)
    public void crunchEachDayAtMidnight() {
        crunchNow();
    }

    public void crunchNow() {
        // lock an hardcoded id
        // perform map reduce operation
        // count how much people logged in
        // each hour that day

        // store this day result
    }

    public boolean isCrunchingNow() {
        // look if hardcoded id is actually locked
        return true;
    }

    public Object getCrunchedDataForAParticularDay(Date day) {
        // retrieve previously stored crunched result for that day
        return null;
    }

    public Date predictTheBestTimeToShutdownServerUsingDataBetween(Date day0, Date day1) {
        return null;
    }
}
