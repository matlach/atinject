package org.atinject.core.resource.entity;

import java.util.Map;

import org.atinject.core.entity.Entity;

public class ResourceEntity extends Entity {

    private static final long serialVersionUID = 1L;

    private String name;
    
    private String bundleName;
    
    private Map<String, ResourceStringEntity> resourceStrings;

	@Override
	public Object getId() {
		return name;
	}
    
}
