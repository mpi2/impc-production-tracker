package org.gentar.common.state_machine;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
public class StatusTransitionDTO extends RepresentationModel
{
    private String currentStatus;
    private List<TransitionDTO> transitions;
}
