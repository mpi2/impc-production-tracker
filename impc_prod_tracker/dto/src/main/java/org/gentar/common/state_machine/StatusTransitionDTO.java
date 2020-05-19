package org.gentar.common.state_machine;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.List;

@Data
public class StatusTransitionDTO
{
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String currentStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TransitionDTO> transitions;

    private String actionToExecute;
}
