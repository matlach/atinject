package org.atinject.core.dto;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;


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
    
    @PostConstruct
    public void initialize(){
        mapper = new ObjectMapper()
        .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
        .setVisibility(PropertyAccessor.GETTER, Visibility.NONE)
        .setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE)
        .setVisibility(PropertyAccessor.SETTER, Visibility.NONE);
    }
    
    public byte[] writeValueAsBytes(DTO value)
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
    
    public String writeValueAsString(DTO value)
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
    
    public <T extends DTO> T readValue(byte[] content)
    {
        try
        {
            return (T) mapper.readValue(content, DTO.class);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public <T extends DTO> T readValue(String content)
    {
        try
        {
            return (T) mapper.readValue(content, DTO.class);
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
