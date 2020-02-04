package org.gentar.biology.colony;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.gentar.biology.strain.StrainDTO;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ColonyDTO
{
    @JsonIgnore
    private Long outcomeId;
    private String name;
    private Boolean genotypingComment;
    private String statusName;

    @JsonProperty("statusDates")
    private List<StatusStampsDTO> statusStampsDTOS;

    @JsonProperty("backgroundStrain")
    private StrainDTO strainDTO;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("distributionCentresAttributes")
    private List<DistributionCentreDTO> distributionCentreDTOS;
}
