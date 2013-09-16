package org.atinject.core.marshallable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;

public class MarshallableObjectCloner {

 // TODO this need to be configured optimally
    private static final ObjectMapper mapper = new ObjectMapper(new SmileFactory());
    
    /**
     * perform clone using serialization technique using jackson smile format
     */
    public static <T extends MarshallableObject> T cloneMarshallableObject(T object){
        try {
            byte[] bytes = mapper.writeValueAsBytes(object);
            return (T) mapper.readValue(bytes, object.getClass());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
