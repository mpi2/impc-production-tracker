package uk.ac.ebi.impc_prod_tracker.service.biology.project.search;

import lombok.Data;

import java.util.List;

@Data
public class SearchFilter
{
    private String field;
    private List<String> values;
}
