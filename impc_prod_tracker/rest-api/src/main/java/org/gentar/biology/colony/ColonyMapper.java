package org.gentar.biology.colony;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.colony.engine.ColonyEvent;
import org.gentar.biology.colony.engine.ColonyState;
import org.gentar.biology.plan.attempt.crispr_attempt.StrainMapper;
import org.gentar.biology.status.StatusService;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.gentar.common.state_machine.TransitionDTO;
import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ColonyMapper implements Mapper<Colony, ColonyDTO>
{
    private EntityMapper entityMapper;
    private StatusService statusService;
    private StrainMapper strainMapper;

    public ColonyMapper(
        EntityMapper entityMapper, StatusService statusService, StrainMapper strainMapper)
    {
        this.entityMapper = entityMapper;
        this.statusService = statusService;
        this.strainMapper = strainMapper;
    }

    @Override
    public ColonyDTO toDto(Colony entity)
    {
        ColonyDTO colonyDTO = entityMapper.toTarget(entity, ColonyDTO.class);
        colonyDTO.setStrainDTO(strainMapper.toDto(entity.getStrain()));
        colonyDTO.setStatusTransitionDTO(buildStatusTransitionDTO(entity));
        return colonyDTO;
    }

    @Override
    public Colony toEntity(ColonyDTO dto)
    {
        Colony colony = entityMapper.toTarget(dto, Colony.class);
        colony.setStrain(strainMapper.toEntity(dto.getStrainDTO()));
        colony.setStatus(statusService.getStatusByName(dto.getStatusName()));
        return colony;
    }

    private StatusTransitionDTO buildStatusTransitionDTO(Colony colony)
    {
        StatusTransitionDTO statusTransitionDTO = new StatusTransitionDTO();
        statusTransitionDTO.setCurrentStatus(colony.getStatus().getName());
        statusTransitionDTO.setTransitions(getTransitions(colony));
        return statusTransitionDTO;
    }

    private List<TransitionDTO> getTransitions(Colony colony)
    {
        List<TransitionDTO> transitionDTOS = new ArrayList<>();
        String currentStatusName = colony.getStatus().getName();
        ProcessState processState = ColonyState.getStateByInternalName(currentStatusName);
        if (processState != null)
        {
            List<ProcessEvent> colonyEvents =
                EnumStateHelper.getAvailableEventsByState(ColonyEvent.getAllEvents(), processState);
            colonyEvents.forEach(x -> {
                TransitionDTO transition = new TransitionDTO();
                transition.setAction(x.getName());
                transition.setDescription(x.getDescription());
                transition.setNextStatus(x.getEndState().getName());
                transition.setNote(x.getTriggerNote());
                transition.setAvailable(x.isTriggeredByUser());
                transitionDTOS.add(transition);
            });
        }
        return transitionDTOS;
    }
}
