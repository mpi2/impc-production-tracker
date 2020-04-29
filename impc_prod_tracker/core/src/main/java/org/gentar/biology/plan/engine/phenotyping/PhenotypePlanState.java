package org.gentar.biology.plan.engine.phenotyping;

import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessState;

import java.util.Arrays;
import java.util.List;

public enum PhenotypePlanState implements ProcessState
{
    PlanCreated("Plan Created"),
    PlanAbandoned("Plan Abandoned"),
    PhenotypingInProgress("Phenotyping In Progress"),
    PhenotypePlanAborted("Phenotyping Plan Aborted");

    private String internalName;

    PhenotypePlanState(String internalName)
    {
        this.internalName = internalName;
    }

    public String getInternalName()
    {
        return internalName;
    }

    public static List<ProcessState> getAllStates()
    {
        return Arrays.asList(PhenotypePlanState.values());
    }

    public static ProcessState getStateByInternalName(String internalName)
    {
        return EnumStateHelper.getStateByInternalName(
                Arrays.asList(PhenotypePlanState.values()), internalName);
    }

    @Override
    public String getName()
    {
        return this.name();
    }
}
