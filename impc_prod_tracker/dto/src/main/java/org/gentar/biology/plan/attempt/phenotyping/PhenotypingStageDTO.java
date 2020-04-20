package org.gentar.biology.plan.attempt.phenotyping;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.gentar.common.state_machine.StatusTransitionDTO;

import java.time.LocalDate;
import java.util.List;

@Data
@RequiredArgsConstructor
public class PhenotypingStageDTO
{
    @JsonIgnore
    private Long id;

    private LocalDate phenotypingExperimentsStarted;
    private Boolean doNotCountTowardsCompleteness;

    private String statusName;

    private String phenotypingTypeName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("statusDates")
    private List<StatusStampsDTO> statusStampsDTOS;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("tissueDistribution")
    private List<TissueDistributionDTO> tissueDistributionCentreDTOs;

    @JsonProperty("statusTransition")
    private StatusTransitionDTO statusTransitionDTO;
}
