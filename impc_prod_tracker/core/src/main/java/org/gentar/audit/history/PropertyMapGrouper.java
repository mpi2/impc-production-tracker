package org.gentar.audit.history;

import org.gentar.audit.diff.ChangeEntry;
import org.gentar.audit.diff.PropertyChecker;
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
    private static final String ELEMENT_IN_A_COLLECTION_IDENTIFIER = "#";

    Map<String, Map<String, ChangeEntry>> getGroupedChanges(List<ChangeEntry> changes)
    {
        List<ChangeEntry> externalEntities = changes.stream()
            .filter(this::isARootExternalEntity).collect(Collectors.toList());

        List<ChangeEntry> childrenProperties = changes.stream()
            .filter(x -> !isARootExternalEntity(x)).collect(Collectors.toList());

        createRootKeysInMap(externalEntities);
        addValuesToMap(childrenProperties);

        return groupedProperties;
    }

    boolean isElementInAList(String propertyName)
    {
        return propertyName.contains(ELEMENT_IN_A_COLLECTION_IDENTIFIER);
    }

    private boolean isARootExternalEntity(ChangeEntry changeEntry)
    {
        return !PropertyChecker.isASimpleValue(changeEntry.getType())
            && !isElementInAList(changeEntry.getProperty());
    }

    private void createRootKeysInMap(List<ChangeEntry> externalEntities)
    {
        groupedProperties.put(ROOT, new HashMap<>());
        externalEntities.forEach(this::addGroupKey);
    }

    private void addGroupKey(ChangeEntry group)
    {
        Map<String, ChangeEntry> initialInfo = new HashMap<>();
        initialInfo.put(group.getProperty(), group);
        groupedProperties.put(group.getProperty(), initialInfo);
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
        } else
        {
            addGroupKey(change);
        }
    }

    private String getKeyForProperty(String property)
    {
        String key;

        if (isElementInAList(property))
        {
            int index = property.lastIndexOf(ELEMENT_IN_A_COLLECTION_IDENTIFIER);
            key = property.substring(0, index);
        }
        else if (property.contains(PROPERTY_NAME_SEPARATOR))
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
