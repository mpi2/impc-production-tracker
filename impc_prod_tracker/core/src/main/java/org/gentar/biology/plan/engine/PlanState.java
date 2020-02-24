package org.gentar.biology.plan.engine;

import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessState;
import java.util.Arrays;
import java.util.List;

public enum PlanState implements ProcessState
{
    MicroInjectionInProgress("Micro-injection In Progress"),
    EmbryosProduced("Embryos produced"),
    FounderObtained("Founder obtained"),
    Aborted("Micro-injection Aborted");

    private String internalName;

    PlanState(String internalName)
    {
        this.internalName = internalName;
    }

    public String getInternalName()
    {
        return internalName;
    }

    public static List<ProcessState> getAllStates()
    {
        return Arrays.asList(PlanState.values());
    }

    public static ProcessState getStateByInternalName(String internalName)
    {
        return EnumStateHelper.getStateByInternalName(
            Arrays.asList(PlanState.values()), internalName);
    }

    @Override
    public String getName()
    {
        return this.name();
    }
}
