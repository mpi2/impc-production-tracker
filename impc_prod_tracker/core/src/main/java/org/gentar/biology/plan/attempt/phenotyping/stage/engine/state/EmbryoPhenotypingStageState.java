package org.gentar.biology.plan.attempt.phenotyping.stage.engine.state;

import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessState;

import java.util.Arrays;
import java.util.List;

public enum EmbryoPhenotypingStageState implements ProcessState
{
    EmbryoPhenotypingProductionRegistered("Registered For Embryo Phenotyping"),
    EmbryoPhenotypingStarted("Embryo Phenotyping Started"),
    EmbryoPhenotypingAllDataSent("Embryo Phenotyping All Data Sent"),
    EmbryoPhenotypingAllDataProcessed("Embryo Phenotyping All Data Processed"),
    EmbryoPhenotypingFinished("Embryo Phenotyping Finished"),
    EmbryoPhenotypeProductionAborted("Embryo Phenotype Production Aborted");

    private String internalName;

    EmbryoPhenotypingStageState(String internalName)
    {
        this.internalName = internalName;
    }

    public static List<ProcessState> getAllStates()
    {
        return Arrays.asList(EmbryoPhenotypingStageState.values());
    }

    public static ProcessState getStateByInternalName(String internalName)
    {
        return EnumStateHelper.getStateByInternalName(
                Arrays.asList(EmbryoPhenotypingStageState.values()), internalName);
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
