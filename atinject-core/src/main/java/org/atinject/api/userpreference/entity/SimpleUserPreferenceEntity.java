package org.atinject.api.userpreference.entity;

public class SimpleUserPreferenceEntity extends UserPreferenceEntity {

    private static final long serialVersionUID = 1L;

    private String value;
    
    public SimpleUserPreferenceEntity(){
        super();
    }

    public String getValue() {
        return value;
    }

    public SimpleUserPreferenceEntity setValue(String value) {
        this.value = value;
        return this;
    }
    
}
