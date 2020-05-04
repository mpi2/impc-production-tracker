package org.gentar.biology.plan.attempt.phenotyping.stage.engine.state;

import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessState;

import java.util.Arrays;
import java.util.List;

public enum HaploessentialPhenotypingStageState implements ProcessState
{
    HaploessentialPhenotypingProductionRegistered("Registered For Haplo-Essential Phenotyping"),
    HaploessentialPhenotypingStarted("Haplo-Essential Phenotyping Started"),
    HaploessentialPhenotypingAllDataSent("Haplo-Essential Phenotyping All Data Sent"),
    HaploessentialPhenotypingAllDataProcessed("Haplo-Essential Phenotyping All Data Processed"),
    HaploessentialPhenotypingFinished("Haplo-Essential Phenotyping Finished"),
    HaploessentialPhenotypeProductionAborted("Haplo-Essential Phenotype Production Aborted");

    private String internalName;

    HaploessentialPhenotypingStageState(String internalName)
    {
        this.internalName = internalName;
    }

    public static List<ProcessState> getAllStates()
    {
        return Arrays.asList(HaploessentialPhenotypingStageState.values());
    }

    public static ProcessState getStateByInternalName(String internalName)
    {
        return EnumStateHelper.getStateByInternalName(
                Arrays.asList(HaploessentialPhenotypingStageState.values()), internalName);
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
