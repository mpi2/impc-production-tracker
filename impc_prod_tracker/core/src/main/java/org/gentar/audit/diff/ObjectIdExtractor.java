package org.gentar.audit.diff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class ObjectIdExtractor
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectIdExtractor.class);
    private static final Long NOT_EXIST_ID = -1L;
    /**
     * This method works under the assumption that the audited object has a property called id and
     * a getter to obtain that value.
     * @return Id value.
     */
    public static Long getObjectId(Object object)
    {
        Long id = null;
        try
        {
            Method getId = object.getClass().getMethod("getId");
            id = Long.parseLong(getId.invoke(object).toString());
        } catch (Exception e)
        {
            LOGGER.info("Error obtaining id for entity "+ object + ". Error: " + e.getMessage());
        }
        return id;
    }
}
