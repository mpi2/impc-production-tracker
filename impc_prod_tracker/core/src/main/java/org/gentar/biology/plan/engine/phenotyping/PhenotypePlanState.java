package org.gentar.biology.plan.engine.phenotyping;

import lombok.Getter;
import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessState;

import java.util.Arrays;
import java.util.List;

@Getter
public enum PhenotypePlanState implements ProcessState
{
    PlanCreated("Plan Created"),
    PlanAbandoned("Plan Abandoned"),
    PhenotypePlanAborted("Phenotyping Plan Aborted");

    private final String internalName;

    PhenotypePlanState(String internalName)
    {
        this.internalName = internalName;
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
