package org.gentar.biology.plan.engine.es_cell;

import lombok.Getter;
import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessState;

import java.util.Arrays;

@Getter
public enum EsCellProductionPlanState implements ProcessState
{
    PlanCreated("Plan Created"),
    PlanAbandoned("Plan Abandoned"),
    AttemptInProgress("Attempt In Progress"),
    ChimerasFounderObtained("Chimeras/Founder Obtained"),
    AttemptAborted("Attempt Aborted");

    private final String internalName;

    EsCellProductionPlanState(String internalName)
    {
        this.internalName = internalName;
    }

    public static ProcessState getStateByInternalName(String internalName)
    {
        return EnumStateHelper.getStateByInternalName(
                Arrays.asList(EsCellProductionPlanState.values()), internalName);
    }

    @Override
    public String getName()
    {
        return this.name();
    }
}
