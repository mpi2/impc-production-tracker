package org.gentar.service.biology.project.search;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum SearchType
{
    BY_GENE("gene"),
    BY_LOCATION("location");

    private String name;

    SearchType(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static SearchType valueOfName(String name) {
        for (SearchType e : values()) {
            if (e.name.equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }

    public static List<String> getValidValuesNames()
    {
        return Arrays.stream(SearchType.values())
            .map(SearchType::getName)
            .collect(Collectors.toList());
    }


}
