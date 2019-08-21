package uk.ac.ebi.impc_prod_tracker.common.cloning;

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
