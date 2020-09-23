package org.gentar.biology.plan.attempt.phenotyping.stage.engine;

import org.gentar.statemachine.ProcessState;

public enum AdultState implements ProcessState
{
    PhenotypingRegistered("Phenotyping Registered"),
    RederivationStarted("Rederivation Started"),
    RederivationComplete("Rederivation Complete"),
    PhenotypingStarted("Phenotyping Started"),
    PhenotypingAllDataSent("Phenotyping All Data Sent"),
    PhenotypingAllDataProcessed("Phenotyping All Data Processed"),
    PhenotypingFinished("Phenotyping Finished"),
    PhenotypeProductionAborted("Phenotype Production Aborted");

    private String internalName;

    AdultState(String internalName)
    {
        this.internalName = internalName;
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
