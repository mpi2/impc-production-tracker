package org.gentar.biology.outcome;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.ColonyDTO;
import org.gentar.biology.colony.engine.ColonyEvent;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanService;
import org.gentar.biology.specimen.SpecimenDTO;
import org.gentar.biology.specimen.engine.SpecimenEvent;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessEvent;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class OutcomeRequestProcessor
{
    private PlanService planService;
    private ColonyRequestProcessor colonyRequestProcessor;

    public OutcomeRequestProcessor(
        PlanService planService, ColonyRequestProcessor colonyRequestProcessor)
    {
        this.planService = planService;
        this.colonyRequestProcessor = colonyRequestProcessor;
    }

    /**
     * Updates an outcome with the information than can be updated in a dto.
     * @param originalOutcome The original outcome.
     * @param outcomeDTO the dto with the new information.
     * @return the updated outcome.
     */
    public Outcome getOutcomeToUpdate(Outcome originalOutcome, OutcomeDTO outcomeDTO)
    {
        if (originalOutcome == null || outcomeDTO == null)
        {
            throw new UserOperationFailedException("Cannot update the outcome");
        }
        Outcome newOutcome = new Outcome(originalOutcome);
        setColony(newOutcome, outcomeDTO.getColonyDTO());
        if (newOutcome.getColony() != null)
        {
            ProcessEvent processEvent = getEventFromRequest(outcomeDTO);
            newOutcome.getColony().setEvent(processEvent);
        }

        return newOutcome;
    }

    private void setColony(Outcome newOutcome, ColonyDTO colonyDTO)
    {
        Colony originalColony = new Colony(newOutcome.getColony());
        Colony newColony = colonyRequestProcessor.getColonyToUpdate(originalColony, colonyDTO);
        newOutcome.setColony(newColony);
    }

    /**
     * Validates that an outcome (identified by its tpo) is related to a plan (identified by its pin)
     * @param pin Plan identifier
     * @param tpo Outcome identifier
     */
    public void validateAssociation(String pin, String tpo)
    {
        boolean associated = false;
        Plan plan =  planService.getNotNullPlanByPin(pin);
        Set<Outcome> outcomes = plan.getOutcomes();
        if (outcomes != null)
        {
            for (Outcome outcome : outcomes)
            {
                if (outcome.getTpo().equals(tpo))
                {
                    associated = true;
                    break;
                }
            }
        }
        if (!associated)
        {
            throw new UserOperationFailedException("Plan " + pin + " does not have an outcome "+ tpo);
        }
    }

    private ProcessEvent getEventFromRequest(OutcomeDTO outcomeDTO)
    {
        StatusTransitionDTO statusTransitionDTO;
        ProcessEvent processEvent = null;
        ColonyDTO colonyDTO = outcomeDTO.getColonyDTO();
        SpecimenDTO specimenDTO = outcomeDTO.getSpecimenDTO();
        if (colonyDTO != null)
        {
            statusTransitionDTO = colonyDTO.getStatusTransitionDTO();
            if (statusTransitionDTO != null)
            {
                String action = statusTransitionDTO.getActionToExecute();
                processEvent = EnumStateHelper.getEventByName(ColonyEvent.getAllEvents(), action);
            }
        }
        else if (specimenDTO != null)
        {
            statusTransitionDTO = specimenDTO.getStatusTransitionDTO();
            if (statusTransitionDTO != null)
            {
                String action = statusTransitionDTO.getActionToExecute();
                processEvent = EnumStateHelper.getEventByName(SpecimenEvent.getAllEvents(), action);
            }
        }
        return processEvent;
    }
}
