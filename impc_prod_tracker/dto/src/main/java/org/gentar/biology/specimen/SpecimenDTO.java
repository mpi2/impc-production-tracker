package org.gentar.biology.specimen;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.gentar.common.state_machine.StatusTransitionDTO;

import java.util.List;

@Data
public class SpecimenDTO
{
    private Long id;
    private String specimenExternalRef;
    private String specimenTypeName;
    private String statusName;

    @JsonProperty("statusDates")
    private List<StatusStampsDTO> statusStampsDTOS;

    @JsonProperty("backgroundStrainName")
    private String strainName;

    @JsonProperty("specimenProperties")
    private List<SpecimenPropertyDTO> specimenPropertyDTOS;

    @JsonProperty("statusTransition")
    private StatusTransitionDTO statusTransitionDTO;
}
