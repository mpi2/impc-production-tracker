package org.gentar.biology.plan.attempt.phenotyping.stage.engine.state;

import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessState;

import java.util.Arrays;
import java.util.List;

public enum EarlyAdultPhenotypingStageState implements ProcessState
{
    PhenotypingProductionRegistered("Phenotyping Production Registered"),
    RederivationStarted("Rederivation Started"),
    RederivationComplete("Rederivation Complete"),
    PhenotypingStarted("Phenotyping Started"),
    PhenotypingDataReceived("Phenotyping Data Received"),
    PhenotypingAllDataSent("Phenotyping All Data Sent"),
    PhenotypingAllDataProcessed("Phenotyping All Data Processed"),
    PhenotypingFinished("Phenotyping Finished"),
    PhenotypeProductionAborted("Phenotype Production Aborted");

    private String internalName;

    EarlyAdultPhenotypingStageState(String internalName)
    {
        this.internalName = internalName;
    }

    public static List<ProcessState> getAllStates()
    {
        return Arrays.asList(EarlyAdultPhenotypingStageState.values());
    }

    public static ProcessState getStateByInternalName(String internalName)
    {
        return EnumStateHelper.getStateByInternalName(
                Arrays.asList(EarlyAdultPhenotypingStageState.values()), internalName);
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
