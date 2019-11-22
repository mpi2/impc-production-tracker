package org.gentar.web.dto.colony;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.gentar.web.dto.colony.DistributionCentresDTO.DistributionCentreDTO;
import org.gentar.web.dto.strain.StrainDTO;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ColonyDTO
{
    @JsonIgnore
    private Long id;
    @JsonIgnore
    private Long outcomeId;
    private String name;
    private Boolean genotypeConfirmed;
    private Boolean genotypingComment;
    private Boolean isReleasedFromGenotyping;

    @JsonProperty("backgroundStrainAttributes")
    private StrainDTO strainDTO;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("distributionCentresAttributes")
    private List<DistributionCentreDTO> distributionCentreDTOS;
}
