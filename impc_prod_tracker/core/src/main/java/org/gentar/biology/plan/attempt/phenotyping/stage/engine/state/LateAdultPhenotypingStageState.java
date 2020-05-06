package org.gentar.biology.plan.attempt.phenotyping.stage.engine.state;

import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessState;

import java.util.Arrays;
import java.util.List;

public enum LateAdultPhenotypingStageState implements ProcessState
{
    RegisteredForLateAdultPhenotypingProduction("Late Adult Phenotyping Registered"),
    LateAdultPhenotypingStarted("Late Adult Phenotyping Started"),
    LateAdultPhenotypingAllDataSent("Late Adult Phenotyping All Data Sent"),
    LateAdultPhenotypingAllDataProcessed("Late Adult Phenotyping All Data Processed"),
    LateAdultPhenotypingFinished("Late Adult Phenotyping Finished"),
    LateAdultPhenotypeProductionAborted("Late Adult Phenotype Production Aborted");

    private String internalName;

    LateAdultPhenotypingStageState(String internalName)
    {
        this.internalName = internalName;
    }

    public static List<ProcessState> getAllStates()
    {
        return Arrays.asList(LateAdultPhenotypingStageState.values());
    }

    public static ProcessState getStateByInternalName(String internalName)
    {
        return EnumStateHelper.getStateByInternalName(
                Arrays.asList(LateAdultPhenotypingStageState.values()), internalName);
    }
    @Override
    public String getName()
    {
        return this.name();
    }

    @Override
    public String getInternalName()
    {
        return internalName;
    }
    
}
