package uk.ac.ebi.impc_prod_tracker.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class ObjectIdExtractor
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectIdExtractor.class);
    /**
     * This method works under the assumption that the audited object has a property called id and
     * a getter to obtain that value.
     * @return Id value.
     */
    public static Long getObjectId(Object object)
    {
        Long id = 0l;
        try
        {
            Method getId = object.getClass().getMethod("getId");
            id = Long.parseLong(getId.invoke(object).toString());
        } catch (Exception e)
        {
            LOGGER.error("Error obtaining id for entity "+ object + ". Error: " + e.getMessage());
            e.printStackTrace();
        }
        return id;
    }
}
