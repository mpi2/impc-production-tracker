package org.gentar.biology.plan.attempt.phenotyping.stage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.gentar.biology.mutation.MutationResponseDTO;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Relation(collectionRelation = "phenotypingStages")
@Data
public class PhenotypingStageResponseDTO extends RepresentationModel<PhenotypingStageResponseDTO>
{
    @JsonIgnore
    private Long id;

    private String pin;
    private String psn;
    private String statusName;
    private String phenotypingTypeName;
    private String phenotypingExternalRef;

    @JsonProperty("statusDates")
    private List<StatusStampsDTO> statusStampsDTOS;

    @JsonProperty("statusTransition")
    private StatusTransitionDTO statusTransitionDTO;

    @JsonUnwrapped
    private PhenotypingStageCommonDTO phenotypingStageCommonDTO;
}
