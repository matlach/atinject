package org.atinject.core.entity;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@ApplicationScoped
public class VersionableEntityObjectMapper
{
    private ObjectMapper mapper;

    @PostConstruct
    public void initialize() {
        mapper = new ObjectMapper()
        .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
        .setVisibility(PropertyAccessor.GETTER, Visibility.NONE)
        .setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE)
        .setVisibility(PropertyAccessor.SETTER, Visibility.NONE);
    }
    
    public void writeObject(ObjectOutput output, VersionableEntity entity, int version) throws IOException {
        output.writeInt(version);
        byte[] json = writeValueAsBytes(entity);
        output.writeInt(json.length);
        output.write(json);
    }
    
    public int readVersion(ObjectInput input) throws IOException {
        return input.readInt();
    }
    
    public byte[] readJSONBytes(ObjectInput input) throws IOException {
        int jsonLength = input.readInt();
        byte[] json = new byte[jsonLength];
        input.read(json);
        return json;
    }
    
    public byte[] writeValueAsBytes(Object value) throws IOException {
    	return mapper.writeValueAsBytes(value);
    }
    
    public String writeValueAsString(Object value) throws IOException {
        return mapper.writeValueAsString(value);
    }
    
    public <T extends VersionableEntity> T readValue(byte[] content, Class<T> valueType) throws IOException {
        return mapper.readValue(content, valueType);
    }
    
    public <T extends VersionableEntity> T readValue(String content, Class<T> valueType) throws IOException {
        return mapper.readValue(content, valueType);
    }

    public JsonNode readTree(byte[] content) throws IOException {
        return mapper.readTree(content);
    }

    public JsonNode readTree(String content) throws IOException {
        return mapper.readTree(content);
    }
}
