package org.gentar.biology.plan.attempt.phenotyping;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStageService;
import org.gentar.biology.plan.attempt.phenotyping.stage.status_stamp.PhenotypingStageStatusStamp;
import org.gentar.biology.plan.attempt.phenotyping.stage.tissue_distribution.TissueDistribution;
import org.gentar.biology.status.StatusMapper;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.gentar.common.state_machine.TransitionDTO;
import org.gentar.statemachine.TransitionEvaluation;
import org.gentar.statemachine.TransitionMapper;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class PhenotypingStageMapper implements Mapper<PhenotypingStage, PhenotypingStageDTO>
{
    private EntityMapper entityMapper;
    private TissueDistributionMapper tissueDistributionMapper;
    private StatusMapper statusMapper;
    private PhenotypingStageTypeMapper phenotypeStageTypeMapper;
    private PhenotypingStageService phenotypingStageService;
    private TransitionMapper transitionMapper;

    public PhenotypingStageMapper(
        EntityMapper entityMapper,
        TissueDistributionMapper tissueDistributionMapper,
        StatusMapper statusMapper,
        PhenotypingStageTypeMapper phenotypeStageTypeMapper,
        PhenotypingStageService phenotypingStageService,
        TransitionMapper transitionMapper)
    {
        this.entityMapper = entityMapper;
        this.tissueDistributionMapper = tissueDistributionMapper;
        this.statusMapper = statusMapper;
        this.phenotypeStageTypeMapper = phenotypeStageTypeMapper;
        this.phenotypingStageService = phenotypingStageService;
        this.transitionMapper = transitionMapper;
    }

    @Override
    public PhenotypingStageDTO toDto(PhenotypingStage phenotypingStage) {
        PhenotypingStageDTO phenotypingStageDTO = entityMapper.toTarget(phenotypingStage, PhenotypingStageDTO.class);
        phenotypingStageDTO.setPhenotypingTypeName(phenotypeStageTypeMapper.toDto(phenotypingStage.getPhenotypingStageType()));
        phenotypingStageDTO.setStatusName(phenotypingStage.getStatus().getName());
        addStatusStamps(phenotypingStageDTO, phenotypingStage);
        phenotypingStageDTO.setTissueDistributionCentreDTOs(tissueDistributionMapper.toDtos(phenotypingStage.getTissueDistributions()));
        phenotypingStageDTO.setStatusTransitionDTO(buildStatusTransitionDTO(phenotypingStage));
        return phenotypingStageDTO;
    }

    private void addStatusStamps(PhenotypingStageDTO phenotypingStageDTO, PhenotypingStage phenotypingStage) {
        Set<PhenotypingStageStatusStamp> statusStamps = phenotypingStage.getPhenotypingStageStatusStamps();
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
    public PhenotypingStage toEntity(PhenotypingStageDTO dto) {
        PhenotypingStage phenotypingStage = entityMapper.toTarget(dto, PhenotypingStage.class);
        setPhenotypingStageType(phenotypingStage, dto);
        setTissueDistributionCentre(phenotypingStage, dto);

        // TODO check this part. Add the first status programmatically.
        if (phenotypingStage.getStatus() == null)
        {
            if (phenotypingStage.getPhenotypingStageType().getId() == 1)
            {
                phenotypingStage.setStatus(statusMapper.toEntity("Phenotyping Production Registered"));
            } else if (phenotypingStage.getPhenotypingStageType().getId() == 2)
            {
                phenotypingStage.setStatus(statusMapper.toEntity("Registered For Late Adult Phenotyping Production"));
            }
        }

        return phenotypingStage;
    }

    @Override
    public Set<PhenotypingStage> toEntities(Collection<PhenotypingStageDTO> dtos) {
        Set<PhenotypingStage> phenotypingStages = new HashSet<>();
        if (dtos != null)
        {
            dtos.forEach(phenotypingStageDTO -> phenotypingStages.add(toEntity(phenotypingStageDTO)));
        }

        return phenotypingStages;
    }

    private void setPhenotypingStageType(PhenotypingStage phenotypingStage, PhenotypingStageDTO phenotypingStageDTO) {
        phenotypingStage.setPhenotypingStageType(phenotypeStageTypeMapper.toEntity(phenotypingStageDTO.getPhenotypingTypeName()));
    }

    private void setTissueDistributionCentre(PhenotypingStage phenotypingStage, PhenotypingStageDTO phenotypingStageDTO) {
        Set<TissueDistribution> tissueDistributions = tissueDistributionMapper.toEntities(phenotypingStageDTO.getTissueDistributionCentreDTOs());
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

