package org.atinject.core.profiling;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.interceptor.InvocationContext;

import org.atinject.core.concurrent.Asynchronous;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;


@ApplicationScoped
public class ProfilingService {

    private MetricRegistry metrics;
    private Counter totalConcurrentInvocation;
    private Meter totalInvocation;
    private Meter totalException;
    
    @PostConstruct
    public void initialize() {
    	metrics = new MetricRegistry();
    	totalConcurrentInvocation = metrics.counter("totalConcurrentInvocation");
    	totalInvocation = metrics.meter("totalInvocation");
    	totalException = metrics.meter("totalException");
    }
    
    @Asynchronous
    public void beforeInvocation(InvocationContext invocationContext) {
    	totalConcurrentInvocation.inc();
    	totalInvocation.mark();
    }
    
    @Asynchronous
    public void afterInvocation(InvocationContext invocationContext, Exception exception) {
    	totalConcurrentInvocation.dec();
    	if (exception != null) {
    		totalException.mark();
    	}
    }
    
    public Context getTimerContext(InvocationContext invocationContext) {
    	return metrics.timer(getTimerMetricName(invocationContext)).time();
    }
    
    public String getTimerMetricName(InvocationContext invocationContext) {
    	return new StringBuilder()
    		.append(invocationContext.getMethod().getDeclaringClass().getName())
    		.append("::")
    		.append(invocationContext.getMethod().getName())
    		.toString();
    }
    
    public long getTotalConcurrentInvocationCount() {
    	return totalConcurrentInvocation.getCount();
    }
    
    public long getTotalInvocationCount() {
		return totalInvocation.getCount();
	}

	public double getTotalInvocationFifteenMinuteRate() {
		return totalInvocation.getFifteenMinuteRate();
	}

	public double getTotalInvocationFiveMinuteRate() {
		return totalInvocation.getFiveMinuteRate();
	}

	public double getTotalInvocationMeanRate() {
		return totalInvocation.getMeanRate();
	}

	public double getTotalInvocationOneMinuteRate() {
		return totalInvocation.getOneMinuteRate();
	}
	
    public long getTotalExceptionCount() {
		return totalException.getCount();
	}

	public double getTotalExceptionFifteenMinuteRate() {
		return totalException.getFifteenMinuteRate();
	}

	public double getTotalExceptionFiveMinuteRate() {
		return totalException.getFiveMinuteRate();
	}

	public double getTotalExceptionMeanRate() {
		return totalException.getMeanRate();
	}

	public double getTotalExceptionOneMinuteRate() {
		return totalException.getOneMinuteRate();
	}

	public List<Timer> getTopXMostInvokedMethods(int x) {
    	return metrics.getTimers().values()
    			.stream()
	    		.sorted((timer1, timer2) -> {
	    			if (timer1.getCount() < timer2.getCount()) {
	    				return -1;
	    			}
	    			if (timer1.getCount() > timer2.getCount()) {
	    				return 1;
	    			}
	    			return 0;
	    		})
	    		.limit(x)
	    		.collect(Collectors.toList());
    }
    
    public List<Timer> getTopXSlowestMethods(int x) {
    	return metrics.getTimers().values()
    			.stream()
	    		.sorted((timer1, timer2) -> {
	    			if (timer1.getMeanRate() < timer2.getMeanRate()) {
	    				return -1;
	    			}
	    			if (timer1.getMeanRate() > timer2.getMeanRate()) {
	    				return 1;
	    			}
	    			return 0;
	    		})
	    		.limit(x)
	    		.collect(Collectors.toList());
    }
    
}
