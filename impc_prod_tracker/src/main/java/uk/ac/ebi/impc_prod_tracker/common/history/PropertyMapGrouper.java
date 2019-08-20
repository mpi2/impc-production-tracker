package uk.ac.ebi.impc_prod_tracker.common.history;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.impc_prod_tracker.common.diff.ChangeEntry;
import uk.ac.ebi.impc_prod_tracker.common.diff.PropertyChecker;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class in charge of transforming a list of {@link ChangeEntry} into a map
 * in a way that the properties that are from nested objects are grouped
 * under that object. The properties that are not part of a nested object are under the "root" entry
 * in the map.
 */
class PropertyMapGrouper
{
    static final String ROOT = "root";
    private Map<String, Map<String, ChangeEntry>> groupedProperties = new HashMap<>();
    private static final String PROPERTY_NAME_SEPARATOR = ".";
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyMapGrouper.class);

    Map<String, Map<String, ChangeEntry>> getGroupedChanges(List<ChangeEntry> changes)
    {
        List<ChangeEntry> externalEntities = changes.stream()
            .filter(this::isARootExternalEntity).collect(Collectors.toList());
        List<ChangeEntry> childrenProperties = changes.stream()
            .filter(x -> !isARootExternalEntity(x)).collect(Collectors.toList());
        try
        {
            createRootKeysInMap(externalEntities);
            addValuesToMap(childrenProperties);
        }
        catch(IllegalArgumentException iae)
        {
            String errorMessage = iae.getMessage() + ". The current changes: " + changes;
            LOGGER.error(errorMessage);

        }
        return groupedProperties;
    }

    private boolean isARootExternalEntity(ChangeEntry changeEntry)
    {
        return !PropertyChecker.isASimpleValue(changeEntry.getType())
            && !PropertyChecker.isCollection(changeEntry.getType());
    }

    private void createRootKeysInMap(List<ChangeEntry> externalEntities)
    {
        groupedProperties.put(ROOT, new HashMap<>());

        externalEntities.forEach(x -> {
            Map<String, ChangeEntry> rootKeyInfo = new HashMap<>();
            rootKeyInfo.put(x.getProperty(), x);
            groupedProperties.put(x.getProperty(), rootKeyInfo);
        });
    }

    private void addValuesToMap(List<ChangeEntry> childrenProperties)
    {
        childrenProperties.forEach(this::addPropertyToMap);
    }

    private void addPropertyToMap(ChangeEntry change) throws IllegalArgumentException
    {
        String key = getKeyForProperty(change.getProperty());

        if (groupedProperties.containsKey(key))
        {
            Map<String, ChangeEntry> entityGrouper = groupedProperties.get(key);
            entityGrouper.put(change.getProperty(), change);
        }
        else
        {
            String errorMessage = "Grouping property " + change.getProperty()
                + ". No parent information was found (key " + key + "). " +
                "It seems that the changes detector didn't detect all  the needed properties.";
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private String getKeyForProperty(String property)
    {
        String key;
        if (property.contains(PROPERTY_NAME_SEPARATOR))
        {
            int index = property.lastIndexOf(PROPERTY_NAME_SEPARATOR);
            key = property.substring(0, index);
        }
        else
        {
            key = ROOT;
        }
        return key;
    }

    void print()
    {
        groupedProperties.forEach((k, v) ->
        {
            System.out.println(k);
            System.out.println(" { ");
            v.forEach((k1, v1) -> {
                System.out.println("    " + k1 + " = " + v1);
            });
            System.out.println("  } ");
        });
    }
}
