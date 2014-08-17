package org.atinject.api.userpreference.entity;

public class SimpleUserPreferenceEntity extends UserPreferenceEntity {

    private static final long serialVersionUID = 1L;

    private String value;
    
    public String getValue() {
        return value;
    }

    public SimpleUserPreferenceEntity setValue(String value) {
        this.value = value;
        return this;
    }

	@Override
	public Object getId() {
		// TODO Auto-generated method stub
		return null;
	}
    
}
