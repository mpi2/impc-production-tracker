package uk.ac.ebi.impc_prod_tracker.common.diff;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Util class to check properties related information.
 */
class PropertyChecker
{
    private static final PropertyUtilsBean PROPERTY_UTILS_BEAN = new PropertyUtilsBean();
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyChecker.class);

    static List<String> getPropertyNamesList(Object object)
    {
        List<String> propertyNamesList = new ArrayList<>();

        new BeanMap(object).keySet().stream()
            .filter(x -> !"class".equals(x.toString()))
            .forEach(x -> propertyNamesList.add(x.toString()));

        return propertyNamesList;
    }

    static List<String> getPropertiesByType(Class<?> type)
    {
        List<String> propertyNamesList = new ArrayList<>();
        Field[] fields = type.getDeclaredFields();
        System.out.println(fields);

        Arrays.asList(fields).stream()
            .filter(x -> !x.getName().startsWith("this$"))
            .forEach(x ->
            {
                propertyNamesList.add(x.getName());
                System.out.println(x);
            });

        return propertyNamesList;
    }

    static List<PropertyDefinition> getPropertiesDataByType(Class<?> type)
    {
        List<PropertyDefinition> dataList = new ArrayList<>();
        Field[] fields = type.getDeclaredFields();

        Arrays.asList(fields).stream()
            .filter(x -> !x.getName().startsWith("this$"))
            .forEach(x ->
            {
                PropertyDefinition data = new PropertyDefinition();
                data.setName(x.getName());
                data.setType(x.getType());
                dataList.add(data);
            });

        return dataList;
    }

    static Class<?> getPropertyType(Object object, String propertyName)
    {
        Class<?> type = null;
        try
        {
            type = PROPERTY_UTILS_BEAN.getPropertyType(object, propertyName);
        } catch (Exception e)
        {
            LOGGER.error("Error getting getPropertyType for property : "
                + propertyName + ". Error: " + e.getMessage());
        }
        return type;
    }

    static Object getValue(String property, Object object)
    {
        Object value = null;
        if (object != null)
        {
            try
            {
                value = PROPERTY_UTILS_BEAN.getProperty(object, property);
            }
            catch (Exception e)
            {
                LOGGER.error("Error getting value for property : " + property + ". Error: " + e.getMessage());
            }
        }
        return value;
    }

    static boolean isCollection(Class<?> type)
    {
        return Collection.class.isAssignableFrom(type);
    }

    static boolean isASimpleValue(Class<?> type)
    {
        return BeanUtils.isSimpleValueType(type);
    }
}
