package org.gentar.util;

import com.google.gson.Gson;

public class Cloner
{
    @SuppressWarnings("unchecked")
    public static Object cloneThroughJson(Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return gson.fromJson(json, object.getClass());
    }
}
