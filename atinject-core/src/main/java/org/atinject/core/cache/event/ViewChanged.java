package org.atinject.core.cache.event;

import java.util.List;
import java.util.stream.Collectors;

import org.atinject.core.event.Event;
import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;
import org.infinispan.remoting.transport.TopologyAwareAddress;

public class ViewChanged extends Event {
	
    private static final long serialVersionUID = 1L;
    
    private TopologyAwareAddress localAddress;
    private List<TopologyAwareAddress> newMembers;
    private List<TopologyAwareAddress> oldMembers;
    private int viewId;
    
    public TopologyAwareAddress getLocalAddress() {
		return localAddress;
	}

	public List<TopologyAwareAddress> getNewMembers() {
		return newMembers;
	}

	public List<TopologyAwareAddress> getOldMembers() {
		return oldMembers;
	}

	public int getViewId() {
		return viewId;
	}

	public ViewChanged setEvent(ViewChangedEvent event) {
    	localAddress = (TopologyAwareAddress) event.getLocalAddress();
    	newMembers = event.getNewMembers().stream()
    			.map(member -> (TopologyAwareAddress) member)
    			.collect(Collectors.toList());
    	oldMembers = event.getOldMembers().stream()
    			.map(member -> (TopologyAwareAddress) member)
    			.collect(Collectors.toList());
        viewId = event.getViewId();
        return this;
    }
    
}
