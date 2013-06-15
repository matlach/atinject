package org.atinject.api.systemproperty;

import java.lang.management.RuntimeMXBean;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.tiers.Service;
import org.slf4j.Logger;

@ApplicationScoped
public class SystemPropertyService extends Service {

	@Inject Logger logger;
	
	@Inject RuntimeMXBean runtimeMXBean;
	
	@PostConstruct
	public void initialize(){
		// TODO move these checks in a separate @Startup @ApplicationScoped class
		// TODO add configuration to halt service if not provided
		// TODO find the actual value through jmx if not provided
		if (! isXmsArgumentProvided()){
			logger.warn("-Xms argument has not been provided, it's a best practice to do so");
		}
		
		if (! isXmxArgumentProvided()){
			logger.warn("-Xmx argument has not been provided, it's a best practice to do so");
		}
	}
	
	public List<String> getInputArguments(){
		return runtimeMXBean.getInputArguments();
	}
	
	public String getXmsArgument(){
		return getArgument("-Xms");
	}
	
	public boolean isXmsArgumentProvided(){
		return getXmsArgument() != null;
	}
	
	public String getXmxArgument(){
		return getArgument("-Xmx");
	}
	
	public boolean isXmxArgumentProvided(){
		return getXmxArgument() != null;
	}
	
	public String getNewRatio(){
		return getArgument("-XX:NewRatio");
	}
	
	public boolean isNewRatioProvided(){
		return getNewRatio() != null;
	}
	
	public String getAggressiveOpts(){
		return getArgument("-XX:+AggressiveOpts");
	}
	
	public boolean isAggressiveOptsProvided(){
		return getAggressiveOpts() != null;
	}
	
	public String getCompileThreshold(){
		return getArgument("-XX:CompileThreshold");
	}
	
	public boolean isCompileThresholdProvided(){
		return getCompileThreshold() != null;
	}
	
	private String getArgument(String argumentName){
		for (String argument : getInputArguments()){
			if (argument.startsWith(argumentName)){
				return argument;
			}
		}
		return null;
	}
	
}
