package org.atinject.api.user.entity;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.atinject.api.user.entity.UserEntity.UserExternalizer;
import org.atinject.core.cdi.CDI;
import org.atinject.core.entity.VersionableEntity;
import org.infinispan.commons.marshall.Externalizer;
import org.infinispan.commons.marshall.SerializeWith;

@SerializeWith(UserExternalizer.class)
public class UserEntity extends VersionableEntity {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;

    public UserEntity() {
        super();
    }

    public String getId() {
        return id;
    }

    public UserEntity setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserEntity setName(String name) {
        this.name = name;
        return this;
    }
    
    public static class UserExternalizer implements Externalizer<UserEntity> {
        private static final long serialVersionUID = 1L;

        private static VersionableUserExternalizer externalizer =
                CDI.select(VersionableUserExternalizer.class).get();
        
        @Override
        public void writeObject(ObjectOutput output, UserEntity user) throws IOException {
            externalizer.writeObject(output, user);
        }

        @Override
        public UserEntity readObject(ObjectInput input) throws IOException, ClassNotFoundException {
            return externalizer.readObject(input);
        }
    }

}
