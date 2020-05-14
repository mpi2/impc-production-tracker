package org.gentar.biology.plan.attempt.phenotyping;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStageService;
import org.gentar.biology.plan.attempt.phenotyping.stage.status_stamp.PhenotypingStageStatusStamp;
import org.gentar.biology.plan.attempt.phenotyping.stage.tissue_distribution.TissueDistribution;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.gentar.common.state_machine.TransitionDTO;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.gentar.statemachine.TransitionMapper;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class PhenotypingStageMapper implements Mapper<PhenotypingStage, PhenotypingStageDTO>
{
    private EntityMapper entityMapper;
    private TissueDistributionMapper tissueDistributionMapper;
    private PhenotypingStageTypeMapper phenotypeStageTypeMapper;
    private PhenotypingStageService phenotypingStageService;
    private TransitionMapper transitionMapper;

    public PhenotypingStageMapper(
        EntityMapper entityMapper,
        TissueDistributionMapper tissueDistributionMapper,
        PhenotypingStageTypeMapper phenotypeStageTypeMapper,
        PhenotypingStageService phenotypingStageService,
        TransitionMapper transitionMapper)
    {
        this.entityMapper = entityMapper;
        this.tissueDistributionMapper = tissueDistributionMapper;
        this.phenotypeStageTypeMapper = phenotypeStageTypeMapper;
        this.phenotypingStageService = phenotypingStageService;
        this.transitionMapper = transitionMapper;
    }

    @Override
    public PhenotypingStageDTO toDto(PhenotypingStage phenotypingStage)
    {
        PhenotypingStageDTO phenotypingStageDTO =
            entityMapper.toTarget(phenotypingStage, PhenotypingStageDTO.class);
        phenotypingStageDTO.setPhenotypingTypeName(
            phenotypeStageTypeMapper.toDto(phenotypingStage.getPhenotypingStageType()));
        phenotypingStageDTO.setStatusName(phenotypingStage.getStatus().getName());
        addStatusStamps(phenotypingStageDTO, phenotypingStage);
        phenotypingStageDTO.setTissueDistributionCentreDTOs(
            tissueDistributionMapper.toDtos(phenotypingStage.getTissueDistributions()));
        phenotypingStageDTO.setStatusTransitionDTO(buildStatusTransitionDTO(phenotypingStage));
        return phenotypingStageDTO;
    }

    private void addStatusStamps(
        PhenotypingStageDTO phenotypingStageDTO, PhenotypingStage phenotypingStage)
    {
        Set<PhenotypingStageStatusStamp> statusStamps =
            phenotypingStage.getPhenotypingStageStatusStamps();
        List<StatusStampsDTO> statusStampsDTOS = new ArrayList<>();
        if (statusStamps != null) {
            statusStamps.forEach(x -> {
                StatusStampsDTO statusStampsDTO = new StatusStampsDTO();
                statusStampsDTO.setStatusName(x.getStatus().getName());
                statusStampsDTO.setDate(x.getDate());
                statusStampsDTOS.add(statusStampsDTO);
            });
        }
        statusStampsDTOS.sort(Comparator.comparing(StatusStampsDTO::getDate));
        phenotypingStageDTO.setStatusStampsDTOS(statusStampsDTOS);
    }

    @Override
    public PhenotypingStage toEntity(PhenotypingStageDTO phenotypingStageDTO)
    {
        PhenotypingStage phenotypingStage =
            entityMapper.toTarget(phenotypingStageDTO, PhenotypingStage.class);
        if (phenotypingStageDTO.getId() == null)
        {
            setPhenotypingStageType(phenotypingStage, phenotypingStageDTO);
        }
        else
        {
            setDataFromPersisted(phenotypingStage, phenotypingStageDTO);
        }
        setEvent(phenotypingStage, phenotypingStageDTO);
        setTissueDistributionCentre(phenotypingStage, phenotypingStageDTO);
        return phenotypingStage;
    }

    private void setEvent(PhenotypingStage phenotypingStage, PhenotypingStageDTO phenotypingStageDTO)
    {
        ProcessEvent processEvent = null;
        StatusTransitionDTO statusTransitionDTO = phenotypingStageDTO.getStatusTransitionDTO();
        if (statusTransitionDTO != null)
        {
            String action = statusTransitionDTO.getActionToExecute();
            processEvent = phenotypingStageService.getProcessEventByName(phenotypingStage, action);
        }
        phenotypingStage.setEvent(processEvent);
    }

    private void setDataFromPersisted(
        PhenotypingStage phenotypingStage, PhenotypingStageDTO phenotypingStageDTO)
    {
        PhenotypingStage persisted =
            phenotypingStageService.getPhenotypingStageById(phenotypingStageDTO.getId());
        phenotypingStage.setStatus(persisted.getStatus());
        phenotypingStage.setPhenotypingStageStatusStamps(persisted.getPhenotypingStageStatusStamps());
        phenotypingStage.setPhenotypingAttempt(persisted.getPhenotypingAttempt());
        phenotypingStage.setPhenotypingStageType(persisted.getPhenotypingStageType());
    }

    private void setPhenotypingStageType(
        PhenotypingStage phenotypingStage, PhenotypingStageDTO phenotypingStageDTO)
    {
        phenotypingStage.setPhenotypingStageType(
            phenotypeStageTypeMapper.toEntity(phenotypingStageDTO.getPhenotypingTypeName()));
    }

    private void setTissueDistributionCentre(
        PhenotypingStage phenotypingStage, PhenotypingStageDTO phenotypingStageDTO)
    {
        Set<TissueDistribution> tissueDistributions =
            tissueDistributionMapper.toEntities(phenotypingStageDTO.getTissueDistributionCentreDTOs());
        tissueDistributions.forEach(x -> x.setPhenotypingStage(phenotypingStage));
        phenotypingStage.setTissueDistributions(tissueDistributions);
    }

    private StatusTransitionDTO buildStatusTransitionDTO(PhenotypingStage phenotypingStage)
    {
        StatusTransitionDTO statusTransitionDTO = new StatusTransitionDTO();
        statusTransitionDTO.setCurrentStatus(phenotypingStage.getStatus().getName());
        statusTransitionDTO.setTransitions(getTransitions(phenotypingStage));
        return statusTransitionDTO;
    }

    private List<TransitionDTO> getTransitions(PhenotypingStage phenotypingStage)
    {
        List<TransitionEvaluation> transitionEvaluations =
            phenotypingStageService.evaluateNextTransitions(phenotypingStage);
        return transitionMapper.toDtos(transitionEvaluations);
    }
}
