package org.atinject.core.distexec;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.concurrent.AsynchronousService;
import org.atinject.core.infinispan.CacheName;
import org.infinispan.Cache;
import org.infinispan.distexec.DefaultExecutorService;
import org.infinispan.distexec.DistributedExecutorService;

@ApplicationScoped
public class UserRequestDistributedExecutor {

	@Inject
	@CacheName("distributed-executor-master-cache-node")
	private Cache<?, ?> masterCacheNode;
	
	@Inject
	private AsynchronousService localExecutorService;

	private DistributedExecutorService distributedExecutorService;
	
	@PostConstruct
	public void initialize(){
		distributedExecutorService = new DefaultExecutorService(masterCacheNode, localExecutorService);
	}
	
	
	public <T> Future<T> submit(Callable<T> task, UserKey input){
		return distributedExecutorService.submit(task, input);
	}
	
}
