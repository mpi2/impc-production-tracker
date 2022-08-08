package org.gentar.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Cloner {
    @SuppressWarnings("unchecked")
    public static Object cloneThroughJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        String json1 = objectMapper.writeValueAsString(object);

        return objectMapper
            .readValue(json1, object.getClass());

    }
}
