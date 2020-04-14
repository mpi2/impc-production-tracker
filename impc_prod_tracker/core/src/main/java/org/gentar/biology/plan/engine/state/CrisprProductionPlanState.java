package org.gentar.biology.plan.engine.state;

import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessState;
import java.util.Arrays;
import java.util.List;

public enum CrisprProductionPlanState implements ProcessState
{
    PlanCreated("Plan Created"),
    AttemptInProgress("Attempt In Progress"),
    EmbryosObtained("Embryos Obtained"),
    GLT("GLT"),
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

    public static List<ProcessState> getAllStates()
    {
        return Arrays.asList(CrisprProductionPlanState.values());
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
