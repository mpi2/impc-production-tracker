package org.gentar.service.biology.project.search;

import lombok.Data;

import java.util.List;

@Data
public class SearchFilter
{
    private String field;
    private List<String> values;
}
