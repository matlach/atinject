package org.atinject.core.cache.event;

import java.util.List;

import org.atinject.core.event.Event;
import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;
import org.infinispan.remoting.transport.TopologyAwareAddress;
import org.infinispan.remoting.transport.jgroups.JGroupsTopologyAwareAddress;

public class ViewChanged extends Event {
	
    private static final long serialVersionUID = 1L;
    
    private JGroupsTopologyAwareAddress localAddress;
    private List<JGroupsTopologyAwareAddress> newMembers;
    private List<JGroupsTopologyAwareAddress> oldMembers;
    private int viewId;
    
    public TopologyAwareAddress getLocalAddress() {
		return localAddress;
	}

	public List<JGroupsTopologyAwareAddress> getNewMembers() {
		return newMembers;
	}

	public List<JGroupsTopologyAwareAddress> getOldMembers() {
		return oldMembers;
	}

	public int getViewId() {
		return viewId;
	}

	public ViewChanged setEvent(ViewChangedEvent event) {
//    	localAddress = (JGroupsTopologyAwareAddress) event.getLocalAddress();
//    	newMembers = event.getNewMembers().stream()
//    			.map(member -> (JGroupsTopologyAwareAddress) member)
//    			.collect(Collectors.toList());
//    	oldMembers = event.getOldMembers().stream()
//    			.map(member -> (JGroupsTopologyAwareAddress) member)
//    			.collect(Collectors.toList());
        viewId = event.getViewId();
        return this;
    }
    
}
