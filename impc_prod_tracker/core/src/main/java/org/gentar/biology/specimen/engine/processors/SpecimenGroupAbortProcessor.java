package org.gentar.biology.specimen.engine.processors;

import org.gentar.biology.specimen.Specimen;
import org.gentar.biology.specimen.engine.SpecimenStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;

public class SpecimenGroupAbortProcessor extends AbstractProcessor
{
    public SpecimenGroupAbortProcessor(SpecimenStateSetter specimenStateSetter)
    {
        super(specimenStateSetter);
    }

    @Override
    protected boolean canExecuteTransition(ProcessData entity)
    {
        return canAbortSpecimen((Specimen) entity);
    }

    private boolean canAbortSpecimen(Specimen specimen)
    {
        // Put here the validations before aborting a Specimen.
        return true;
    }
}

