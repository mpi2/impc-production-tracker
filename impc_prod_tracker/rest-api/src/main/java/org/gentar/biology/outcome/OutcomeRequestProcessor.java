package org.gentar.biology.outcome;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.ColonyDTO;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanService;
import org.gentar.exceptions.UserOperationFailedException;
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
     * @param outcomeUpdateDTO the dto with the new information.
     * @return the updated outcome.
     */
    public Outcome getOutcomeToUpdate(Outcome originalOutcome, OutcomeUpdateDTO outcomeUpdateDTO)
    {
        if (originalOutcome == null || outcomeUpdateDTO == null)
        {
            throw new UserOperationFailedException("Cannot update the outcome");
        }
        Outcome newOutcome = new Outcome(originalOutcome);
        if (outcomeUpdateDTO.getOutcomeCommonDTO() != null)
        {
            setColony(newOutcome, outcomeUpdateDTO.getOutcomeCommonDTO().getColonyDTO());
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
}
