package org.atinject.core.json;

import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * Use of helper class here is unfortunate, but necessary; alternative would be to use ThreadLocal, and set instance
 * before calling serialization. Benefit of that approach would be dynamic configuration; however, this approach is
 * easier to demonstrate.
 */
public class MapperHolder
{
    private final ObjectMapper mapper = new ObjectMapper();
    private final static MapperHolder instance = new MapperHolder();

    public static ObjectMapper mapper()
    {
        return instance.mapper;
    }
}
