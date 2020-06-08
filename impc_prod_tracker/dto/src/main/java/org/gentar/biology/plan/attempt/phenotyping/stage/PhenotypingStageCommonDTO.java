package org.gentar.biology.plan.attempt.phenotyping.stage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class PhenotypingStageCommonDTO
{
    private LocalDate phenotypingExperimentsStarted;
    private Boolean doNotCountTowardsCompleteness;
    private LocalDate initialDataReleaseDate;

    @JsonProperty("tissueDistributions")
    private List<TissueDistributionDTO> tissueDistributionCentreDTOs;
}
