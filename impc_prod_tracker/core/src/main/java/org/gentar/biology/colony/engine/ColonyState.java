package org.gentar.biology.colony.engine;

import org.gentar.statemachine.ProcessState;
import java.util.HashMap;
import java.util.Map;

public enum ColonyState implements ProcessState
{
    GenotypeInProgress("Genotype In Progress"),
    GenotypeNotConfirmed("Genotype Not Confirmed"),
    GenotypeConfirmed("Genotype Confirmed"),
    GenotypeExtinct("Genotype Extinct"),
    ColonyAborted("Colony Aborted");

    private static final Map<String, ColonyState> BY_INTERNAL_NAME = new HashMap<>();

    static
    {
        for (ColonyState e: values())
        {
            BY_INTERNAL_NAME.put(e.internalName, e);
        }
    }

    private final String internalName;

    ColonyState(String internalName)
    {
        this.internalName = internalName;
    }

    public static ProcessState getStateByInternalName(String internalName)
    {
        return BY_INTERNAL_NAME.get(internalName);
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
