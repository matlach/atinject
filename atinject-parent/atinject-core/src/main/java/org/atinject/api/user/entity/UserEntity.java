package org.atinject.api.user.entity;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.atinject.api.user.entity.UserEntity.UserExternalizer;
import org.atinject.core.entity.AbstractEntity;
import org.infinispan.marshall.Externalizer;
import org.infinispan.marshall.SerializeWith;

@SerializeWith(UserExternalizer.class)
public class UserEntity extends AbstractEntity
{
    private static final long serialVersionUID = 1L;

    private static final int CURRENT_VERSION = 1;
    
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
            output.writeInt(CURRENT_VERSION);
            // jackson smile ? json ?
            output.writeObject(user.uuid);
            output.writeObject(user.name);
            output.writeObject(user.password);
        }

        @Override
        public UserEntity readObject(ObjectInput input) throws IOException, ClassNotFoundException
        {
            int version = input.readInt();
            UserEntity user = new UserEntity();
            switch (version)
            {
                // case ANY_OLD_VERSION : ..
                case CURRENT_VERSION:
                    
                    user.uuid = (String) input.readObject();
                    user.name = (String) input.readObject();
                    user.password = (String) input.readObject();
                    return user;
            }
            throw new IOException("bad version '" + version + "'");
        }
    }

}
