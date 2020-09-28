package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanService;
import org.gentar.biology.plan.attempt.phenotyping.stage.tissue_distribution.TissueDistribution;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.statemachine.ProcessEvent;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PhenotypingStageRequestProcessor
{
    private final PhenotypingStageService phenotypingStageService;
    private final PlanService planService;
    private final TissueDistributionMapper tissueDistributionMapper;

    public PhenotypingStageRequestProcessor(
        PhenotypingStageService phenotypingStageService,
        PlanService planService,
        TissueDistributionMapper tissueDistributionMapper)
    {
        this.phenotypingStageService = phenotypingStageService;
        this.planService = planService;
        this.tissueDistributionMapper = tissueDistributionMapper;
    }

    /**
     * Updates a phenotyping stage with the information than can be updated in a dto.
     *
     * @param originalPhenotypingStage  The original phenotyping stage.
     * @param phenotypingStageUpdateDTO the dto with the new information.
     * @return the updated phenotyping stage.
     */
    public PhenotypingStage getPhenotypingStageToUpdate(
        PhenotypingStage originalPhenotypingStage, PhenotypingStageUpdateDTO phenotypingStageUpdateDTO)
    {
        if (originalPhenotypingStage == null || phenotypingStageUpdateDTO == null)
        {
            throw new UserOperationFailedException("Cannot update the phenotyping stage");
        }
        PhenotypingStage newPhenotypingStage = new PhenotypingStage(originalPhenotypingStage);
        PhenotypingStageCommonDTO phenotypingStageCommonDTO =
            phenotypingStageUpdateDTO.getPhenotypingStageCommonDTO();
        newPhenotypingStage.setInitialDataReleaseDate(
            phenotypingStageCommonDTO.getInitialDataReleaseDate());
        newPhenotypingStage.setPhenotypingExperimentsStarted(
            phenotypingStageCommonDTO.getPhenotypingExperimentsStarted());
        Set<TissueDistribution> mappedTissueDistributions =
            tissueDistributionMapper.toEntities(
                phenotypingStageCommonDTO.getTissueDistributionCentreDTOs());
        associateTissueDistributions(
            newPhenotypingStage, originalPhenotypingStage, mappedTissueDistributions);
        setEvent(newPhenotypingStage, phenotypingStageUpdateDTO);
        return newPhenotypingStage;
    }

    private void associateTissueDistributions(
        PhenotypingStage phenotypingStageToUpdate,
        PhenotypingStage originalPhenotypingStage,
        Set<TissueDistribution> tissueDistributions)
    {
        if (tissueDistributions != null)
        {
            // PhenotypingStage never changes so we keep the original reference
            tissueDistributions.forEach(x -> x.setPhenotypingStage(originalPhenotypingStage));
        }
        phenotypingStageToUpdate.setTissueDistributions(tissueDistributions);
    }

    private void setEvent(PhenotypingStage phenotypingStage, PhenotypingStageUpdateDTO phenotypingStageUpdateDTO)
    {
        if (phenotypingStageUpdateDTO.getStatusTransitionDTO() != null)
        {
            String action = phenotypingStageUpdateDTO.getStatusTransitionDTO().getActionToExecute();
            ProcessEvent processEvent = phenotypingStageService.getProcessEventByName(phenotypingStage, action);
            phenotypingStage.setEvent(processEvent);
        }
    }

    /**
     * Validates that an phenotyping stage (identified by its psn) is related to a plan (identified by its pin)
     *
     * @param pin Plan identifier
     * @param psn Phenotyping stage identifier
     */
    public void validateAssociation(String pin, String psn)
    {
        boolean associated = false;
        Plan plan = planService.getNotNullPlanByPin(pin);
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
            throw new UserOperationFailedException("Plan " + pin + " does not have an phenotyping stages " + psn);
        }
    }
}
