package org.atinject.core.json;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.core.dto.BaseDTO;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@ApplicationScoped
public class DTOObjectMapper
{
    private ObjectMapper mapper;
    
    public void initialize(){
        mapper = new ObjectMapper()
        .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
        .setVisibility(PropertyAccessor.GETTER, Visibility.NONE)
        .setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE)
        .setVisibility(PropertyAccessor.SETTER, Visibility.NONE);
    }
    
    public byte[] writeValueAsBytes(BaseDTO value)
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
    
    public String writeValueAsString(BaseDTO value)
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
    
    public <T extends BaseDTO> T readValue(byte[] content)
    {
        try
        {
            return (T) mapper.readValue(content, BaseDTO.class);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public <T extends BaseDTO> T readValue(String content)
    {
        try
        {
            return (T) mapper.readValue(content, BaseDTO.class);
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
