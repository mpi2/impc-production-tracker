package uk.ac.ebi.impc_prod_tracker.controller.project.plan.phenotype_plan.phenotyping_production;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PhenotypingProductionDTO
{
    private String colonyName;
    private String colonyStrainName;
    private LocalDateTime experimentStartedOn;
    private Boolean started;
    private Boolean complete;
    private Boolean doNotCountTowardsCompleteness;
    @JsonProperty("tissueDistributionCentres")
    private List<TissueDistributionCentreDTO> tissueDistributionCentreDTOs;
}
