package org.atinject.core.resource.entity;

import java.util.Map;

import org.atinject.core.entity.Entity;

public class ResourceEntity extends Entity {

    private static final long serialVersionUID = 1L;

    String id;
    
    String bundleId;
    
    Map<String, ResourceStringEntity> resourceStrings;

	@Override
	public Object getId() {
		// TODO Auto-generated method stub
		return null;
	}
    
}
