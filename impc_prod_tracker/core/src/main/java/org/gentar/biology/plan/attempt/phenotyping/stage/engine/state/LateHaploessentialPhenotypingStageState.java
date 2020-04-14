package org.gentar.biology.plan.attempt.phenotyping.stage.engine.state;

import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessState;

import java.util.Arrays;
import java.util.List;

public enum LateHaploessentialPhenotypingStageState implements ProcessState
{
    LateHaploessentialPhenotypingProductionRegistered("Registered For Late Haplo-Essential Phenotyping"),
    LateHaploessentialPhenotypingStarted("Late Haplo-Essential Phenotyping Started"),
    LateHaploessentialPhenotypingDataReceived("Late Haplo-Essential Phenotyping Data Received"),
    LateHaploessentialPhenotypingAllDataSent("Late Haplo-Essential Phenotyping All Data Sent"),
    LateHaploessentialPhenotypingAllDataProcessed("Late Haplo-Essential Phenotyping All Data Processed"),
    LateHaploessentialPhenotypingFinished("Late Haplo-Essential Phenotyping Finished"),
    LateHaploessentialPhenotypeProductionAborted("Late Haplo-Essential Phenotype Production Aborted");

    private String internalName;

    LateHaploessentialPhenotypingStageState(String internalName)
    {
        this.internalName = internalName;
    }

    public static List<ProcessState> getAllStates()
    {
        return Arrays.asList(LateHaploessentialPhenotypingStageState.values());
    }

    public static ProcessState getStateByInternalName(String internalName)
    {
        return EnumStateHelper.getStateByInternalName(
                Arrays.asList(LateHaploessentialPhenotypingStageState.values()), internalName);
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
