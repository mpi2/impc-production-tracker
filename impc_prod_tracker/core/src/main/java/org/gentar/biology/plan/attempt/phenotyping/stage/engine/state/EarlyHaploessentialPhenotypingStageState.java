package org.gentar.biology.plan.attempt.phenotyping.stage.engine.state;

import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessState;

import java.util.Arrays;
import java.util.List;

public enum EarlyHaploessentialPhenotypingStageState implements ProcessState
{
    EarlyHaploessentialPhenotypingProductionRegistered("Registered For Early Haplo-Essential Phenotyping"),
    EarlyHaploessentialPhenotypingStarted("Early Haplo-Essential Phenotyping Started"),
    EarlyHaploessentialPhenotypingDataReceived("Early Haplo-Essential Phenotyping Data Received"),
    EarlyHaploessentialPhenotypingAllDataSent("Early Haplo-Essential Phenotyping All Data Sent"),
    EarlyHaploessentialPhenotypingAllDataProcessed("Early Haplo-Essential Phenotyping All Data Processed"),
    EarlyHaploessentialPhenotypingFinished("Early Haplo-Essential Phenotyping Finished"),
    EarlyHaploessentialPhenotypeProductionAborted("Early Haplo-Essential Phenotype Production Aborted");

    private String internalName;

    EarlyHaploessentialPhenotypingStageState(String internalName)
    {
        this.internalName = internalName;
    }

    public static List<ProcessState> getAllStates()
    {
        return Arrays.asList(EarlyHaploessentialPhenotypingStageState.values());
    }

    public static ProcessState getStateByInternalName(String internalName)
    {
        return EnumStateHelper.getStateByInternalName(
                Arrays.asList(EarlyHaploessentialPhenotypingStageState.values()), internalName);
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
