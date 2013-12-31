package org.atinject.core.entity;

import java.io.Serializable;

public abstract class Entity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object obj) {
        throw new AssertionError("equals method should be overriden");
    }

    @Override
    public int hashCode() {
        throw new AssertionError("hashCode method should be overriden");
    }
}
