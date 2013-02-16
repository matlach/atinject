package org.atinject.api.user.entity;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.atinject.api.user.entity.UserEntity.UserExternalizer;
import org.atinject.core.cdi.BeanManagerExtension;
import org.atinject.core.entity.VersionableEntity;
import org.infinispan.marshall.Externalizer;
import org.infinispan.marshall.SerializeWith;

@SerializeWith(UserExternalizer.class)
public class UserEntity extends VersionableEntity
{
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String password;

    public UserEntity()
    {
        super();
    }

    public String getId()
    {
        return id;
    }

    public UserEntity setId(String id)
    {
        this.id = id;
        return this;
    }

    public String getName()
    {
        return name;
    }

    public UserEntity setName(String name)
    {
        this.name = name;
        return this;
    }

    public String getPassword()
    {
        return password;
    }

    public UserEntity setPassword(String password)
    {
        this.password = password;
        return this;
    }

    public static class UserExternalizer implements Externalizer<UserEntity>
    {
        private static final long serialVersionUID = 1L;

        @Override
        public void writeObject(ObjectOutput output, UserEntity user) throws IOException
        {
            VersionableUserExternalizer externalizer = BeanManagerExtension.getReference(VersionableUserExternalizer.class);
            externalizer.writeObject(output, user);
        }

        @Override
        public UserEntity readObject(ObjectInput input) throws IOException, ClassNotFoundException
        {
            VersionableUserExternalizer externalizer = BeanManagerExtension.getReference(VersionableUserExternalizer.class);
            return externalizer.readObject(input);
        }
    }

}
