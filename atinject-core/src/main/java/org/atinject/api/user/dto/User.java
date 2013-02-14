package org.atinject.api.user.dto;

import org.atinject.core.dto.BaseDTO;

public class User extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String password;

    public User()
    {
        super();
    }

    public String getId()
    {
        return id;
    }

    public User setId(String id)
    {
        this.id = id;
        return this;
    }

    public String getName()
    {
        return name;
    }

    public User setName(String name)
    {
        this.name = name;
        return this;
    }

    public String getPassword()
    {
        return password;
    }

    public User setPassword(String password)
    {
        this.password = password;
        return this;
    }
}
