package org.gentar.common.state_machine;

import lombok.Data;

@Data
public class TransitionDTO
{
    private String action;
    private String description;
    private boolean triggeredByUser;
    private boolean available;
    private String note;
    private String nextStatus;
}
