package org.atinject.api.user.dto;

import java.util.UUID;

import org.atinject.core.dto.DTO;

public class User extends DTO {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private String name;

    public User() {
        super();
    }

    public UUID getId() {
        return id;
    }

    public User setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

}
