package org.atinject.api.user.entity;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.entity.VersionableEntityExternalizer;
import org.atinject.core.entity.VersionableEntityObjectMapper;

@ApplicationScoped
public class VersionableUserExternalizer implements VersionableEntityExternalizer<UserEntity>
{
    
    private static final int CURRENT_VERSION = 1;
    
    @Inject
    private VersionableEntityObjectMapper objectMapper;
    
    @Override
    public void writeObject(ObjectOutput output, UserEntity user) throws IOException
    {
        objectMapper.writeObject(output, user, CURRENT_VERSION);
    }

    @Override
    public UserEntity readObject(ObjectInput input) throws IOException, ClassNotFoundException
    {
        int version = objectMapper.readVersion(input);
        byte[] json = objectMapper.readJSONBytes(input);
        
        switch (version)
        {
            // case ANY_OLD_VERSION :
            // read json as tree
            // then reconstruct object as needed ex:
            // JsonNode node = JSon.readTree(json);
            // UserEntity user = new UserEntity();
            // user.setName(node.get("name"));
            case CURRENT_VERSION:
                // just leave everything to jackson
                return objectMapper.readValue(json, UserEntity.class);
        }
        
        throw new IOException("bad version '" + version + "'");
    }

}
