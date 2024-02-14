package org.gentar.biology.plan.engine.crispr;

import lombok.Getter;
import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessState;
import java.util.Arrays;

@Getter
public enum CrisprProductionPlanState implements ProcessState
{
    PlanCreated("Plan Created"),
    PlanAbandoned("Plan Abandoned"),
    AttemptInProgress("Attempt In Progress"),
    EmbryosObtained("Embryos Obtained"),
    FounderObtained("Founder Obtained"),
    AttemptAborted("Attempt Aborted");

    private final String internalName;

    CrisprProductionPlanState(String internalName)
    {
        this.internalName = internalName;
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
