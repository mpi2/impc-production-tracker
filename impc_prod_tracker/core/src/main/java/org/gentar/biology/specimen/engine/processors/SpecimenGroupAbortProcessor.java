package org.gentar.biology.specimen.engine.processors;

import org.gentar.biology.specimen.Specimen;
import org.gentar.biology.specimen.engine.SpecimenStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

@Component
public class SpecimenGroupAbortProcessor extends AbstractProcessor
{
    public SpecimenGroupAbortProcessor(SpecimenStateSetter specimenStateSetter)
    {
        super(specimenStateSetter);
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);
        boolean canAbortSpecimen = canAbortSpecimen((Specimen) data);
        transitionEvaluation.setExecutable(canAbortSpecimen);
        if (!canAbortSpecimen)
        {
            transitionEvaluation.setNote("Specimen cannot be aborted [Not implemented yet.]");
        }
        return transitionEvaluation;
    }

    private boolean canAbortSpecimen(Specimen specimen)
    {
        // Put here the validations before aborting a Specimen.
        return false;
    }
}

