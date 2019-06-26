package uk.ac.ebi.impc_prod_tracker.common.diff;

import lombok.Data;

@Data
public class ChangeEntry
{
    private String property;
    private String oldValue;
    private String newValue;
}
