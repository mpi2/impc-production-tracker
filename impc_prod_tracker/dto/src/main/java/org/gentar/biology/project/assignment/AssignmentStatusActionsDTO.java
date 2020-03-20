package org.gentar.biology.project.assignment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.gentar.common.actions.ActionDTO;
import java.util.List;

@Data
public class AssignmentStatusActionsDTO
{
    @JsonProperty("availableActions")
    private List<ActionDTO> actionDTOS;

    private String actionToExecute;
}
