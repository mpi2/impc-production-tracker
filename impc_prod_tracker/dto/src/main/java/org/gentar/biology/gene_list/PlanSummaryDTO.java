package org.gentar.biology.gene_list;

import lombok.Data;

import java.util.List;

@Data
public class PlanSummaryDTO
{
    private String pin;
    private String typeName;
    private String workUnitName;
    private String statusName;
    private List<OutcomeSummaryDTO> outcomes;
}
