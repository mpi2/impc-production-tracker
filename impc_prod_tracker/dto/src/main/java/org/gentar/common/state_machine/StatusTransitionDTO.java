package org.gentar.common.state_machine;

import lombok.Data;
import java.util.List;

@Data
public class StatusTransitionDTO
{
    private String currentStatus;
    private List<TransitionDTO> transitions;
    private String actionToExecute;
}
