package org.atinject.core.versioning;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public interface VersionableEntityExternalizer<T>
{

    void writeObject(ObjectOutput output, T object) throws IOException;
    
    T readObject(ObjectInput input) throws IOException, ClassNotFoundException;
}
