package org.atinject.core.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JSon
{
    private static final ObjectMapper mapper;
    static{
        mapper = new ObjectMapper()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    private JSon(){}
    
    public static byte[] writeValueAsBytes(Object value)
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
    
    public static String writeValueAsString(Object value)
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
    
    public static <T> T readValue(byte[] content, Class<T> valueType)
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
    
    public static <T> T readValue(String content, Class<T> valueType)
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

    public static JsonNode readTree(byte[] content)
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

    public static JsonNode readTree(String content)
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
