package org.atinject.api.user.entity;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.atinject.api.user.entity.UserEntity.UserExternalizer;
import org.atinject.core.cdi.BeanManagerExtension;
import org.atinject.core.entity.AbstractEntity;
import org.infinispan.marshall.Externalizer;
import org.infinispan.marshall.SerializeWith;

@SerializeWith(UserExternalizer.class)
public class UserEntity extends AbstractEntity
{
    private static final long serialVersionUID = 1L;

    private String uuid;
    private String name;
    private String password;

    public UserEntity()
    {
        super();
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
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
