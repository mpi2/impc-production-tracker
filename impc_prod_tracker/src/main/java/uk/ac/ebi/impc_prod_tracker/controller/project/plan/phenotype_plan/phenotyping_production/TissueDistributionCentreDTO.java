package uk.ac.ebi.impc_prod_tracker.controller.project.plan.phenotype_plan.phenotyping_production;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TissueDistributionCentreDTO
{
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String centreName;
    private String materialTypeName;
}
