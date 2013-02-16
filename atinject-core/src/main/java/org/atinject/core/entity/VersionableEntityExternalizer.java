package org.atinject.core.entity;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


public interface VersionableEntityExternalizer<T extends VersionableEntity>
{

    void writeObject(ObjectOutput output, T object) throws IOException;
    
    T readObject(ObjectInput input) throws IOException, ClassNotFoundException;
}
