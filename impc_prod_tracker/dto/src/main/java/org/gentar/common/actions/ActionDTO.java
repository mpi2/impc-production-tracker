package org.gentar.common.actions;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This class represents an action that a user executes on a entity.
 */
@Data
@AllArgsConstructor
public class ActionDTO
{
    private String actionName;
    private String description;

}
