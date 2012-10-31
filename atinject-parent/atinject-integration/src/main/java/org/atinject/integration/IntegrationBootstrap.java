package org.atinject.integration;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.atinject.api.user.UserServiceImpl;
import org.atinject.api.user.dto.GetUserRequest;
import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.user.event.UserAdded;
import org.atinject.core.cdi.Weld;
import org.atinject.core.configuration.Configuration;
import org.atinject.core.distexec.UserRequestDistributedExecutor;
import org.atinject.core.infinispan.CacheName;
import org.atinject.core.websocket.WebSocketClient;
import org.atinject.core.websocket.WebSocketEndpoint;
import org.atinject.core.websocket.WebSocketServer;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.jboss.weld.environment.se.bindings.Parameters;
import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class IntegrationBootstrap {

	public static void main(String[] args) throws InterruptedException, IOException{
	    System.setProperty("user.timezone", "GMT");
	    
	    
		final Weld weld = new Weld();
		weld.initialize();
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
			@Override
			public void run() {
				weld.shutdown();
			}}));
		
		System.in.read();
		System.exit(0);
	}
	
	@Inject
	private EmbeddedCacheManager embeddedCacheManager;
	
	@Inject
	@CacheName
	private Cache<String, String> defaultCache;
	
	@Inject
	private UserRequestDistributedExecutor distributedExecutor;
	
	@Inject
	@Configuration
	private String myConfiguration;
	
	@Inject
	private UserServiceImpl userService;
	
	@Inject @WebSocketEndpoint(uri="ws://localhost:8080/websocket")
	private WebSocketClient client;
	
	@Inject @WebSocketEndpoint(uri="ws://localhost:8080/websocket")
	private WebSocketServer server;
	
	public void onContainerInitialized(@Observes ContainerInitialized event, @Parameters List<String> parameters) throws Exception
	{
		Logger logger = LoggerFactory.getLogger(IntegrationBootstrap.class);
		
		// core test
		logger.debug("", embeddedCacheManager);
		logger.debug("", distributedExecutor);
		logger.info(myConfiguration);
		
		userService.fireUserAdded(new UserAdded());
		
		userService.async();
		
		System.out.println(UUID.randomUUID().toString());
		System.out.println(UUID.randomUUID().toString().length());
		System.out.println(UUID.randomUUID().toString().getBytes().length);
		
		UserEntity user = new UserEntity();
		user.setUuid("abc");
		userService.addUser(user);
		
		client.sendPing();
		client.send(new GetUserRequest());
	}
}
