package uk.ac.ebi.impc_prod_tracker.web.dto.common.filters;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FilterDTO
{
    private String name;
    private List<String> values;
}
