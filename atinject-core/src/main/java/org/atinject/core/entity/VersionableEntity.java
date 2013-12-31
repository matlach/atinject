package org.atinject.core.entity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="_class")
public abstract class VersionableEntity extends Entity {
    
    private static final long serialVersionUID = 1L;

}
