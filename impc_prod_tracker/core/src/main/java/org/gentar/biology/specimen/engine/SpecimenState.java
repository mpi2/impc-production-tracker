package org.gentar.biology.specimen.engine;

import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessState;

import java.util.Arrays;
import java.util.List;

public enum SpecimenState implements ProcessState
{
    GenotypeNotConfirmed("Genotype Not Confirmed"),
    GenotypeConfirmed("Genotype Confirmed");

    private String internalName;

    SpecimenState(String internalName)
    {
        this.internalName = internalName;
    }

    public static List<ProcessState> getAllStates()
    {
        return Arrays.asList(org.gentar.biology.specimen.engine.SpecimenState.values());
    }

    public static ProcessState getStateByInternalName(String internalName)
    {
        return EnumStateHelper.getStateByInternalName(
                Arrays.asList(org.gentar.biology.specimen.engine.SpecimenState.values()), internalName);
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

