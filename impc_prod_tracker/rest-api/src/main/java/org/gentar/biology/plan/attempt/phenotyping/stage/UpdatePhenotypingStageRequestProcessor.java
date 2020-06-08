package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanService;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UpdatePhenotypingStageRequestProcessor
{
    private PlanService planService;

    public UpdatePhenotypingStageRequestProcessor(PlanService planService)
    {
        this.planService = planService;
    }

    /**
     * Validates that an phenotyping stage (identified by its id) is related to a plan (identified by its pin)
     * @param pin Plan identifier
     * @param id Phenotyping Stage identifier
     */
    public void validateAssociation(String pin, String id)
    {
        boolean associated = false;
        Plan plan =  planService.getNotNullPlanByPin(pin);
        Set<PhenotypingStage> phenotypingStages = plan.getPhenotypingAttempt().getPhenotypingStages();
        if (phenotypingStages != null)
        {
            for (PhenotypingStage phenotypingStage : phenotypingStages)
            {
                if (phenotypingStage.getId().equals(id))
                {
                    associated = true;
                    break;
                }
            }
        }
        if (!associated)
        {
            throw new UserOperationFailedException("Plan " + pin + " does not have an phenotyping stage "+ id);
        }
    }

    /**
     * Updates a phenotyping stage with the information than can be updated in a dto.
     * @param originalPhenotypingStage The original phenotyping stage.
     * @param phenotypingStageDTO the dto with the new information.
     * @return the updated phenotyping stage.
     */
    public PhenotypingStage getPhenotypeStageToUpdate(PhenotypingStage originalPhenotypingStage, PhenotypingStageDTO phenotypingStageDTO)
    {
        if (originalPhenotypingStage == null || phenotypingStageDTO == null)
        {
            throw new UserOperationFailedException("Cannot update the phenotyping stage.");
        }
        PhenotypingStage newPhenotypingStage = new PhenotypingStage(originalPhenotypingStage);
        return newPhenotypingStage;
    }
}
