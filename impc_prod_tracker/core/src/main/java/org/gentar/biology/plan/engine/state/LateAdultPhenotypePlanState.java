package org.gentar.biology.plan.engine.state;

import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessState;

import java.util.Arrays;
import java.util.List;

public enum LateAdultPhenotypePlanState implements ProcessState
{
    RegisteredForLateAdultPhenotypingProduction("Registered for Late Adult Phenotyping Production"),
    LateAdultPhenotypingStarted("Late Adult Phenotyping Started"),
    LateAdultPhenotypingDataReceived("Late Adult Phenotyping Data Received"),
    LateAdultPhenotypingAllDataSent("Late Adult Phenotyping All Data Sent"),
    LateAdultPhenotypingFinished("Late Adult Phenotyping Finished"),
    LateAdultPhenotypeProductionAborted("Late Adult Phenotype Production Aborted");

    private String internalName;

    LateAdultPhenotypePlanState(String internalName)
    {
        this.internalName = internalName;
    }

    public String getInternalName()
    {
        return internalName;
    }

    public static List<ProcessState> getAllStates()
    {
        return Arrays.asList(LateAdultPhenotypePlanState.values());
    }

    public static ProcessState getStateByInternalName(String internalName)
    {
        return EnumStateHelper.getStateByInternalName(
                Arrays.asList(LateAdultPhenotypePlanState.values()), internalName);
    }

    @Override
    public String getName()
    {
        return this.name();
    }
}
