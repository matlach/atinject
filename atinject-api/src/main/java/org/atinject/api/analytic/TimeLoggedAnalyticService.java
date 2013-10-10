package org.atinject.api.analytic;

import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.atinject.api.authentication.event.UserLoggedIn;
import org.atinject.core.affinity.LocalRandomUUIDGenerator;
import org.atinject.core.timer.Schedule;
import org.infinispan.Cache;

@ApplicationScoped
public class TimeLoggedAnalyticService {
	
	@Inject LocalRandomUUIDGenerator idGenerator;
	
	// distributed cache to store raw data
	// elements will expire each day as they are crunched each day
	Cache raw;
	
	// distributed cache to store crunched data
	Cache crunched;
	
	public void onUserLogged(@Observes UserLoggedIn event) {
		// generate local event id
		String key = idGenerator.getKey();
		long t = System.currentTimeMillis();
		raw.put(key, t);
	}
	
	@Schedule(clustered=true)
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
	
	public Object getCrunchedDataForAParticularDay(Date day) {
		// retrieve previously stored crunched result for that day
		return null;
	}
	
	public Object predictTheBestTimeToShutdownServerUsingDataBetween(Date day0, Date day1) {
		return null;
	}
}
