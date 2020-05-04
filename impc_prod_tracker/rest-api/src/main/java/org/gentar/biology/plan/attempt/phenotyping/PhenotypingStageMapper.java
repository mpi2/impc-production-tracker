package org.gentar.biology.plan.attempt.phenotyping;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStageTypes;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.events.EarlyAdultEvent;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.events.HaploessentialEvent;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.events.LateAdultEvent;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.events.EmbryoEvent;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.state.EarlyAdultPhenotypingStageState;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.state.HaploessentialPhenotypingStageState;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.state.LateAdultPhenotypingStageState;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.state.EmbryoPhenotypingStageState;
import org.gentar.biology.plan.attempt.phenotyping.stage.status_stamp.PhenotypingStageStatusStamp;
import org.gentar.biology.plan.attempt.phenotyping.stage.tissue_distribution.TissueDistribution;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageType;
import org.gentar.biology.status.StatusMapper;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.gentar.common.state_machine.TransitionDTO;
import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PhenotypingStageMapper implements Mapper<PhenotypingStage, PhenotypingStageDTO> {
    private EntityMapper entityMapper;
    private TissueDistributionMapper tissueDistributionMapper;
    private StatusMapper statusMapper;
    private PhenotypingStageTypeMapper phenotypeStageTypeMapper;

    public PhenotypingStageMapper(
            EntityMapper entityMapper,
            TissueDistributionMapper tissueDistributionMapper,
            StatusMapper statusMapper,
            PhenotypingStageTypeMapper phenotypeStageTypeMapper
    ) {
        this.entityMapper = entityMapper;
        this.tissueDistributionMapper = tissueDistributionMapper;
        this.statusMapper = statusMapper;
        this.phenotypeStageTypeMapper = phenotypeStageTypeMapper;
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

    private StatusTransitionDTO buildStatusTransitionDTO(PhenotypingStage phenotypingStage) {
        StatusTransitionDTO statusTransitionDTO = new StatusTransitionDTO();
        statusTransitionDTO.setCurrentStatus(phenotypingStage.getStatus().getName());
        statusTransitionDTO.setTransitions(getTransitionsByPhenotypingStageType(phenotypingStage));
        return statusTransitionDTO;
    }

    private List<TransitionDTO> getTransitionsByPhenotypingStageType(PhenotypingStage phenotypingStage) {
        List<TransitionDTO> transitionDTOS = new ArrayList<>();
        String currentStatusName = phenotypingStage.getStatus().getName();
        PhenotypingStageType phenotypingStageType = phenotypingStage.getPhenotypingStageType();

        if (phenotypingStageType != null) {

            if (PhenotypingStageTypes.EARLY_ADULT.getTypeName().equalsIgnoreCase(phenotypingStageType.getName())) {
                setEarlyAdultPhenotypingStageTransitions(transitionDTOS, currentStatusName);
            } else if (PhenotypingStageTypes.LATE_ADULT.getTypeName().equalsIgnoreCase(phenotypingStageType.getName())) {
                setLateAdultPhenotypingStageTransitions(transitionDTOS, currentStatusName);
            } else if (PhenotypingStageTypes.HAPLOESSENTIAL.getTypeName().equalsIgnoreCase(phenotypingStageType.getName())) {
                setHaploessentialPhenotypingStageTransitions(transitionDTOS, currentStatusName);
            } else if (PhenotypingStageTypes.EMBRYO.getTypeName().equalsIgnoreCase(phenotypingStageType.getName())) {
                setLateHaploessentialPhenotypingStageTransitions(transitionDTOS, currentStatusName);
            }

        }
        return transitionDTOS;
    }

    private void setEarlyAdultPhenotypingStageTransitions(List<TransitionDTO> transitionDTOS, String currentStatusName) {

        ProcessState phenotypingStageState = EarlyAdultPhenotypingStageState.getStateByInternalName(currentStatusName);
        if (phenotypingStageState != null) {
            List<ProcessEvent> phenotypingStageEvents = EnumStateHelper.getAvailableEventsByState(
                    EarlyAdultEvent.getAllEvents(), phenotypingStageState);
            setTransitions(transitionDTOS, phenotypingStageEvents);
        }
    }

    private void setLateAdultPhenotypingStageTransitions(List<TransitionDTO> transitionDTOS, String currentStatusName) {

        ProcessState phenotypingStageState = LateAdultPhenotypingStageState.getStateByInternalName(currentStatusName);
        if (phenotypingStageState != null) {
            List<ProcessEvent> phenotypingStageEvents = EnumStateHelper.getAvailableEventsByState(
                    LateAdultEvent.getAllEvents(), phenotypingStageState);
            setTransitions(transitionDTOS, phenotypingStageEvents);
        }
    }

    private void setHaploessentialPhenotypingStageTransitions(List<TransitionDTO> transitionDTOS, String currentStatusName) {

        ProcessState phenotypingStageState = HaploessentialPhenotypingStageState.getStateByInternalName(currentStatusName);
        if (phenotypingStageState != null) {
            List<ProcessEvent> phenotypingStageEvents = EnumStateHelper.getAvailableEventsByState(
                    HaploessentialEvent.getAllEvents(), phenotypingStageState);
            setTransitions(transitionDTOS, phenotypingStageEvents);
        }
    }

    private void setLateHaploessentialPhenotypingStageTransitions(List<TransitionDTO> transitionDTOS, String currentStatusName) {

        ProcessState phenotypingStageState = EmbryoPhenotypingStageState.getStateByInternalName(currentStatusName);
        if (phenotypingStageState != null) {
            List<ProcessEvent> phenotypingStageEvents = EnumStateHelper.getAvailableEventsByState(
                    EmbryoEvent.getAllEvents(), phenotypingStageState);
            setTransitions(transitionDTOS, phenotypingStageEvents);
        }
    }

    private void setTransitions(List<TransitionDTO> transitionDTOS, List<ProcessEvent> phenotypingStageEvents) {
        phenotypingStageEvents.forEach(x -> {
            TransitionDTO transition = new TransitionDTO();
            transition.setAction(x.getName());
            transition.setDescription(x.getDescription());
            transition.setNextStatus(x.getEndState().getName());
            transition.setNote(x.getTriggerNote());
            transition.setAvailable(x.isTriggeredByUser());
            transitionDTOS.add(transition);
        });
    }
}

