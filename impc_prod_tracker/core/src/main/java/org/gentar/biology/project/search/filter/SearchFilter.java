package org.gentar.biology.project.search.filter;

import lombok.Data;

import java.util.List;

@Data
public class SearchFilter
{
    private String field;
    private List<String> values;
}
