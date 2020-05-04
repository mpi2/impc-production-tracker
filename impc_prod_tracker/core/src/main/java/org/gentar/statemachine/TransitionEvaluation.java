package org.gentar.statemachine;

import lombok.Data;

/**
 * An object in this class has information about whether or not a transition can be executed and
 * in case it cannot, why.
 */
@Data
public class TransitionEvaluation
{
    private ProcessEvent transition;
    private boolean executable;
    private String note;
}
