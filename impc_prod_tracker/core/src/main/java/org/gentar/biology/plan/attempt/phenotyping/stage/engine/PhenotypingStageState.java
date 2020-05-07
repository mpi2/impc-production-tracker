package org.gentar.biology.plan.attempt.phenotyping.stage.engine;

import org.gentar.biology.plan.attempt.phenotyping.stage.engine.state.EmbryoPhenotypingStageState;
import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessState;
import java.util.Arrays;

public enum PhenotypingStageState implements ProcessState
{
    PhenotypingProductionRegistered("Phenotyping Registered"),
    PhenotypingStarted("Phenotyping Started"),
    PhenotypingAllDataSent("Phenotyping All Data Sent"),
    PhenotypingAllDataProcessed("Phenotyping All Data Processed"),
    PhenotypingFinished("Phenotyping Finished"),
    PhenotypeProductionAborted("Phenotype Production Aborted");

    private String internalName;

    PhenotypingStageState(String internalName)
    {
        this.internalName = internalName;
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
