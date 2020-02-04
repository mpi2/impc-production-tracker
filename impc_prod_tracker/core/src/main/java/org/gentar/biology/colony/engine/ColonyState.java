package org.gentar.biology.colony.engine;

import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessState;
import java.util.Arrays;
import java.util.List;

public enum ColonyState implements ProcessState
{
    GenotypeNotConfirmed("Genotype Not Confirmed"),
    GenotypeConfirmed("Genotype Confirmed");

    private String internalName;

    ColonyState(String internalName)
    {
        this.internalName = internalName;
    }

    public static List<ProcessState> getAllStates()
    {
        return Arrays.asList(ColonyState.values());
    }

    public static ProcessState getStateByInternalName(String internalName)
    {
        return EnumStateHelper.getStateByInternalName(
            Arrays.asList(ColonyState.values()), internalName);
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
