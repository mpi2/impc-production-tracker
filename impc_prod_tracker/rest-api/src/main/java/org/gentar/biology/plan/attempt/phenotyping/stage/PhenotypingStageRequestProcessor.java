package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanService;
import org.gentar.biology.plan.attempt.phenotyping.stage.material_deposited_type.MaterialDepositedType;
import org.gentar.biology.plan.attempt.phenotyping.stage.tissue_distribution.TissueDistribution;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.organization.work_unit.WorkUnitMapper;
import org.gentar.statemachine.ProcessEvent;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PhenotypingStageRequestProcessor
{
    private PhenotypingStageService phenotypingStageService;
    private PlanService planService;
    private PhenotypingStageUpdateMapper phenotypingStageUpdateMapper;
    private TissueDistributionMapper tissueDistributionMapper;
    private WorkUnitMapper workUnitMapper;
    private MaterialDepositedTypeMapper materialDepositedTypeMapper;

    public PhenotypingStageRequestProcessor(PhenotypingStageService phenotypingStageService,
                                            PlanService planService,
                                            PhenotypingStageUpdateMapper phenotypingStageUpdateMapper,
                                            TissueDistributionMapper tissueDistributionMapper, WorkUnitMapper workUnitMapper, MaterialDepositedTypeMapper materialDepositedTypeMapper)
    {
        this.phenotypingStageService = phenotypingStageService;
        this.planService = planService;
        this.phenotypingStageUpdateMapper = phenotypingStageUpdateMapper;
        this.tissueDistributionMapper = tissueDistributionMapper;
        this.workUnitMapper = workUnitMapper;
        this.materialDepositedTypeMapper = materialDepositedTypeMapper;
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

    private void setTissueDistribution(PhenotypingStage newPhenotypingStage,
                                       List<TissueDistributionDTO> tissueDistributionDTOS)
    {

        Set<TissueDistribution> newTissueDistributions = new HashSet<TissueDistribution>();

        tissueDistributionDTOS.forEach(tissueDistributionDTO -> newTissueDistributions.add(
                        getTissueDistributionToUpdate(newPhenotypingStage, tissueDistributionDTO)));

        newPhenotypingStage.setTissueDistributions(newTissueDistributions);
    }

    public TissueDistribution getTissueDistributionToUpdate(PhenotypingStage newPhenotypingStage,
                                                  TissueDistributionDTO tissueDistributionDTO)
    {
        if (tissueDistributionDTO.getId() == null)
        {
            TissueDistribution tissueDistributionToBeCreated = tissueDistributionMapper.toEntity(tissueDistributionDTO);
            tissueDistributionToBeCreated.setPhenotypingStage(newPhenotypingStage);
            return tissueDistributionToBeCreated;
        }

        Set<TissueDistribution> originalTissueDistributions = new HashSet<TissueDistribution>(
        newPhenotypingStage.getTissueDistributions());

        TissueDistribution tissueDistributionToUpdate = originalTissueDistributions.stream()
                .filter(tissueDistribution -> tissueDistributionDTO.getId().equals(tissueDistribution.getId()))
                .findFirst()
                .get();

        tissueDistributionToUpdate.setStartDate(tissueDistributionDTO.getStartDate());
        tissueDistributionToUpdate.setEndDate(tissueDistributionDTO.getEndDate());
        tissueDistributionToUpdate.setMaterialDepositedType(materialDepositedTypeMapper.toEntity(
                tissueDistributionDTO.getMaterialDepositedTypeName()));
        tissueDistributionToUpdate.setWorkUnit(workUnitMapper.toEntity(tissueDistributionDTO.getWorkUnitName()));

        return tissueDistributionToUpdate;
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
