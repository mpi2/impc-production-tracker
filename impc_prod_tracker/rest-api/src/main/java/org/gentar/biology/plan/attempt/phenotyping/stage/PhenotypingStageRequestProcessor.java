package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanService;
import org.gentar.biology.plan.attempt.phenotyping.stage.tissue_distribution.TissueDistribution;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.statemachine.ProcessEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PhenotypingStageRequestProcessor
{
    private PhenotypingStageService phenotypingStageService;
    private PlanService planService;
    private PhenotypingStageUpdateMapper phenotypingStageUpdateMapper;
    private TissueDistributionMapper tissueDistributionMapper;

    public PhenotypingStageRequestProcessor(PhenotypingStageService phenotypingStageService,
                                            PlanService planService,
                                            PhenotypingStageUpdateMapper phenotypingStageUpdateMapper,
                                            TissueDistributionMapper tissueDistributionMapper)
    {
        this.phenotypingStageService = phenotypingStageService;
        this.planService = planService;
        this.phenotypingStageUpdateMapper = phenotypingStageUpdateMapper;
        this.tissueDistributionMapper = tissueDistributionMapper;
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
        if (originalPhenotypingStage == null || originalPhenotypingStage == null)
        {
            throw new UserOperationFailedException("Cannot update the phenotyping stage");
        }
        PhenotypingStage newPhenotypingStage = new PhenotypingStage(originalPhenotypingStage);

        PhenotypingStageCommonDTO phenotypingStageCommonDTO =  phenotypingStageUpdateDTO.getPhenotypingStageCommonDTO();
        if (phenotypingStageCommonDTO.getDoNotCountTowardsCompleteness() != null)
        {
            newPhenotypingStage.setDoNotCountTowardsCompleteness(phenotypingStageCommonDTO.getDoNotCountTowardsCompleteness());
        }
        if (phenotypingStageCommonDTO.getInitialDataReleaseDate() != null)
        {
            newPhenotypingStage.setInitialDataReleaseDate(phenotypingStageCommonDTO.getInitialDataReleaseDate());
        }
        if (phenotypingStageCommonDTO.getPhenotypingExperimentsStarted() != null)
        {
            newPhenotypingStage.setPhenotypingExperimentsStarted(phenotypingStageCommonDTO.getPhenotypingExperimentsStarted());
        }
        if (phenotypingStageCommonDTO.getTissueDistributionCentreDTOs() != null)
        {
            setTissueDistribution(newPhenotypingStage, phenotypingStageCommonDTO.getTissueDistributionCentreDTOs());
        }

        setEvent(newPhenotypingStage, phenotypingStageUpdateDTO);
        return newPhenotypingStage;
    }

    private void setTissueDistribution(PhenotypingStage newPhenotypingStage, List<TissueDistributionDTO> tissueDistributionDTOS)
    {
        Set<TissueDistribution> tissueDistributions = tissueDistributionMapper.toEntities(tissueDistributionDTOS);
        tissueDistributions.forEach(tissueDistribution -> tissueDistribution.setPhenotypingStage(newPhenotypingStage));
        newPhenotypingStage.setTissueDistributions(tissueDistributions);
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
