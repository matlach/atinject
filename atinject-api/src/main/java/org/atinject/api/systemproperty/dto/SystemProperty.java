package org.atinject.api.systemproperty.dto;

import org.atinject.core.dto.DTO;

public class SystemProperty extends DTO {

    private static final long serialVersionUID = 1L;

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public SystemProperty withKey(String key) {
        setKey(key);
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SystemProperty withValue(String value) {
        setValue(value);
        return this;
    }
}
