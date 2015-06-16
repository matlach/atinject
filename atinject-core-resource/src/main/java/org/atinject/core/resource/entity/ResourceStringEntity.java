package org.atinject.core.resource.entity;

import org.atinject.core.entity.Entity;

public class ResourceStringEntity extends Entity {

	private static final long serialVersionUID = 1L;

	private String name;
	
	@Override
	public String getId() {
		return name;
	}

}
