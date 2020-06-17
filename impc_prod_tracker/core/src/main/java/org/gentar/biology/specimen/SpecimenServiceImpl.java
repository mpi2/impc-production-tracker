package org.gentar.biology.specimen;

import org.gentar.biology.specimen.type.SpecimenType;
import org.gentar.biology.specimen.type.SpecimenTypeRepository;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionAvailabilityEvaluator;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpecimenServiceImpl implements SpecimenService
{
    private SpecimenTypeRepository specimenTypeRepository;
    private SpecimenStateMachineResolver specimenStateMachineResolver;
    private TransitionAvailabilityEvaluator transitionAvailabilityEvaluator;

    public SpecimenServiceImpl(
        SpecimenTypeRepository specimenTypeRepository,
        SpecimenStateMachineResolver specimenStateMachineResolver,
        TransitionAvailabilityEvaluator transitionAvailabilityEvaluator)
    {
        this.specimenTypeRepository = specimenTypeRepository;
        this.specimenStateMachineResolver = specimenStateMachineResolver;
        this.transitionAvailabilityEvaluator = transitionAvailabilityEvaluator;
    }

    public SpecimenType getSpecimenTypeByName(String name)
    {
        return specimenTypeRepository.findByNameIgnoreCase(name);
    }

    @Override
    public ProcessEvent getProcessEventByName(Specimen specimen, String name)
    {
        return specimenStateMachineResolver.getProcessEventByActionName(specimen, name);
    }

    @Override
    public List<TransitionEvaluation> evaluateNextTransitions(Specimen specimen)
    {
        List<ProcessEvent> transitions =
            specimenStateMachineResolver.getAvailableTransitionsByEntityStatus(specimen);
        return transitionAvailabilityEvaluator.evaluate(transitions, specimen);
    }
}
