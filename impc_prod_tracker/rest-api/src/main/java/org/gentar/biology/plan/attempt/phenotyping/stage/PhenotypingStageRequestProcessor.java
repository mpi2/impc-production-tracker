package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanService;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PhenotypingStageRequestProcessor
{
    private PlanService planService;

    public PhenotypingStageRequestProcessor(PlanService planService) {
        this.planService = planService;
    }

    /**
     * Updates a phenotyping stage with the information than can be updated in a dto.
     * @param originalPhenotypingStage The original phenotyping stage.
     * @param phenotypingStageUpdateDTO the dto with the new information.
     * @return the updated phenotyping stage.
     */
    public PhenotypingStage getPhenotypingStageToUpdate(PhenotypingStage originalPhenotypingStage,
                                               PhenotypingStageUpdateDTO phenotypingStageUpdateDTO)
    {
        if (originalPhenotypingStage == null || phenotypingStageUpdateDTO == null)
        {
            throw new UserOperationFailedException("Cannot update the phenotyping stage.");
        }
        PhenotypingStage newPhenotypingStage = new PhenotypingStage(originalPhenotypingStage);
        return newPhenotypingStage;
    }

    /**
     * Validates that an phenotyping stage (identified by its psn) is related to a plan (identified by its pin)
     * @param pin Plan identifier
     * @param psn Phenotyping stage identifier
     */
    public void validateAssociation(String pin, String psn)
    {
        boolean associated = false;
        Plan plan =  planService.getNotNullPlanByPin(pin);
        Set<PhenotypingStage> phenotypingStages = plan.getPhenotypingAttempt().getPhenotypingStages();
        if (phenotypingStages != null)
        {
            for (PhenotypingStage phenotypingStage : phenotypingStages)
            {
                if (phenotypingStage.getPsn().equals(psn))
                {
                    associated = true;
                    break;
                }
            }
        }
        if (!associated)
        {
            throw new UserOperationFailedException("Plan " + pin + " does not have an phenotyping stages "+ psn);
        }
    }
}
