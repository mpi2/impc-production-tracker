package org.gentar.biology.plan.engine.crispr;

import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessState;
import java.util.Arrays;

public enum CrisprProductionPlanState implements ProcessState
{
    PlanCreated("Plan Created"),
    AttemptInProgress("Attempt In Progress"),
    EmbryosObtained("Embryos Obtained"),
    FounderObtained("Founder Obtained"),
    AttemptAborted("Attempt Aborted");

    private String internalName;

    CrisprProductionPlanState(String internalName)
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
            Arrays.asList(CrisprProductionPlanState.values()), internalName);
    }

    @Override
    public String getName()
    {
        return this.name();
    }
}
