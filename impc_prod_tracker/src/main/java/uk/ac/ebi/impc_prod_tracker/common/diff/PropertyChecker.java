package uk.ac.ebi.impc_prod_tracker.common.diff;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.Map;

/**
 * Util class to check properties related information.
 */
class PropertyChecker
{
    private static final PropertyUtilsBean PROPERTY_UTILS_BEAN = new PropertyUtilsBean();
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyChecker.class);

    public static Class<?> getPropertyType(Object object, String propertyName)
    {
        Class<?> type = null;
        try
        {
            type = PROPERTY_UTILS_BEAN.getPropertyType(object, propertyName);
        }
        catch (Exception e)
        {
            LOGGER.error("Error getting getPropertyType for property : "
                + propertyName + ". Error: "+ e.getMessage());
        }
        return type;
    }

    public static Object getValue(String property, Object object)
    {
        Object value = null;
        try
        {
            value = PROPERTY_UTILS_BEAN.getProperty(object, property);
        }
        catch (Exception e)
        {
            LOGGER.error("Error getting value for property : " + property + ". Error: "+ e.getMessage());
        }
        return value;
    }

    public static boolean isCollection(Class<?> type) {
        return Collection.class.isAssignableFrom(type);
    }

    public static boolean isASimpleValue(Class<?> type)
    {
        return BeanUtils.isSimpleValueType(type);
    }
}
