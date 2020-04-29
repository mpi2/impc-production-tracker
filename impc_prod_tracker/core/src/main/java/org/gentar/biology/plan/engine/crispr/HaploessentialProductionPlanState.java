package org.gentar.biology.plan.engine.crispr;

import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessState;

import java.util.Arrays;

public enum HaploessentialProductionPlanState implements ProcessState
{
    PlanCreated("Plan Created"),
    AttemptInProgress("Attempt In Progress"),
    EmbryosObtained("Embryos Obtained"),
    AttemptAborted("Attempt Aborted");

    private String internalName;

    HaploessentialProductionPlanState(String internalName)
    {
        this.internalName = internalName;
    }

    public String getInternalName()
    {
        return internalName;
    }

    public static ProcessState getStateByInternalName(String internalName)
    {
        return EnumStateHelper.getStateByInternalName(
                Arrays.asList(HaploessentialProductionPlanState.values()), internalName);
    }

    @Override
    public String getName()
    {
        return this.name();
    }
}
