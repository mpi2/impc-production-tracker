package org.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class JsonConverter
{
    private static ObjectMapper objectMapper;

    static
    {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a z");
        objectMapper.setDateFormat(df);
    }

    public static <T> String toJson(T object) throws JsonProcessingException
    {
        return objectMapper.writeValueAsString(object);
    }
}
