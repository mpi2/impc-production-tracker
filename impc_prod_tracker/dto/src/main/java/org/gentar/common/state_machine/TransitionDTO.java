package org.gentar.common.state_machine;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class TransitionDTO extends RepresentationModel
{
    private String action;
    private String description;
    private boolean available;
    private String note;
    private String nextStatus;
    private String triggerNote;
}
