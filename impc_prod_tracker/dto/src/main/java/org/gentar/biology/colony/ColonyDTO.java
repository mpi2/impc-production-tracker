package org.gentar.biology.colony;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.gentar.common.state_machine.StatusTransitionDTO;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ColonyDTO
{
    @JsonIgnore
    private Long id;

    private String name;

    @JsonIgnore
    private String legacyModification;

    private String genotypingComment;
    private String statusName;

    @JsonProperty("statusDates")
    private List<StatusStampsDTO> statusStampsDTOS;

    @JsonProperty("backgroundStrainName")
    private String strainName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("distributionProducts")
    private List<DistributionProductDTO> distributionProductDTOS;

    @JsonProperty("statusTransition")
    private StatusTransitionDTO statusTransitionDTO;
}
