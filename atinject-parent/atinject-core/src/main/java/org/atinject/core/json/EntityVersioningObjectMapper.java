package org.atinject.core.json;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@ApplicationScoped
public class EntityVersioningObjectMapper
{
    private ObjectMapper mapper;

    @PostConstruct
    public void initialize(){
        mapper = new ObjectMapper()
        .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    public byte[] writeValueAsBytes(Object value)
    {
        try
        {
            return mapper.writeValueAsBytes(value);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public String writeValueAsString(Object value)
    {
        try
        {
            return mapper.writeValueAsString(value);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public <T> T readValue(byte[] content, Class<T> valueType)
    {
        try
        {
            return mapper.readValue(content, valueType);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public <T> T readValue(String content, Class<T> valueType)
    {
        try
        {
            return mapper.readValue(content, valueType);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public JsonNode readTree(byte[] content)
    {
        try
        {
            return mapper.readTree(content);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public JsonNode readTree(String content)
    {
        try
        {
            return mapper.readTree(content);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
