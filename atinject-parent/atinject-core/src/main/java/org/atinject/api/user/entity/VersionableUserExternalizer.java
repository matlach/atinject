package org.atinject.api.user.entity;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.json.EntityVersioningObjectMapper;
import org.atinject.core.versioning.VersionableEntityExternalizer;

@ApplicationScoped
public class VersionableUserExternalizer implements VersionableEntityExternalizer<UserEntity>
{
    
    private static final int CURRENT_VERSION = 1;
    
    @Inject
    private EntityVersioningObjectMapper objectMapper;
    
    @Override
    public void writeObject(ObjectOutput output, UserEntity user) throws IOException
    {
        output.writeInt(CURRENT_VERSION);
        byte[] json = objectMapper.writeValueAsBytes(user);
        output.writeInt(json.length);
        output.write(json);
    }

    @Override
    public UserEntity readObject(ObjectInput input) throws IOException, ClassNotFoundException
    {
        int version = input.readInt();
        int jsonLength = input.readInt();
        byte[] json = new byte[jsonLength];
        input.read(json);
        
        switch (version)
        {
            // case ANY_OLD_VERSION :
            // read json as tree
            // then reconstruct object as needed ex:
            // JsonNode node = JSon.readTree(json);
            // UserEntity user = new UserEntity();
            // user.setName(node.get("name"));
            case CURRENT_VERSION:
                UserEntity user = objectMapper.readValue(json, UserEntity.class);
                return user;
        }
        throw new IOException("bad version '" + version + "'");
    }

}
