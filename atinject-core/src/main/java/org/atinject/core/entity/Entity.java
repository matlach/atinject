package org.atinject.core.entity;

import java.io.Serializable;
import java.util.Objects;

public abstract class Entity implements Serializable {

    private static final long serialVersionUID = 1L;

    public abstract Object getId();
    
    @Override
    public boolean equals(Object obj) {
    	return (obj == this) ||
 	           (obj != null &&
 	           this.getClass().isAssignableFrom(obj.getClass()) &&
 	           obj.getClass().isAssignableFrom(this.getClass())) &&
 	           (Objects.equals(getId(), ((Entity) obj).getId()));
    }

    @Override
    public int hashCode() {
        return Objects.requireNonNull(getId()).hashCode();
    }
}
