package uk.ac.ebi.impc_prod_tracker.controller.project.plan.phenotype_plan.phenotyping_production;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PhenotypingProductionDTO
{
    private String colonyName;
    private String colonyStrainName;
    private LocalDateTime experimentStartedOn;
    private Boolean started;
    private Boolean complete;
    private Boolean doNotCountTowardsCompleteness;
}
