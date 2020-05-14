package org.gentar.biology.colony;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.strain.StrainMapper;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.gentar.common.state_machine.TransitionDTO;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.gentar.statemachine.TransitionMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ColonyMapper implements Mapper<Colony, ColonyDTO>
{
    private EntityMapper entityMapper;
    private StrainMapper strainMapper;
    private ColonyService colonyService;
    private TransitionMapper transitionMapper;

    public ColonyMapper(
        EntityMapper entityMapper,
        StrainMapper strainMapper,
        ColonyService colonyService,
        TransitionMapper transitionMapper)
    {
        this.entityMapper = entityMapper;
        this.strainMapper = strainMapper;
        this.colonyService = colonyService;
        this.transitionMapper = transitionMapper;
    }

    @Override
    public ColonyDTO toDto(Colony entity)
    {
        ColonyDTO colonyDTO = entityMapper.toTarget(entity, ColonyDTO.class);
        colonyDTO.setStrainName(strainMapper.toDto(entity.getStrain()));
        colonyDTO.setStatusTransitionDTO(buildStatusTransitionDTO(entity));
        return colonyDTO;
    }

    @Override
    public Colony toEntity(ColonyDTO colonyDTO)
    {
        Colony colony = entityMapper.toTarget(colonyDTO, Colony.class);
        setEvent(colony, colonyDTO);
        colony.setStrain(strainMapper.toEntity(colonyDTO.getStrainName()));
        return colony;
    }

    private void setEvent(Colony colony, ColonyDTO colonyDTO)
    {
        ProcessEvent processEvent = null;
        StatusTransitionDTO statusTransitionDTO = colonyDTO.getStatusTransitionDTO();
        if (statusTransitionDTO != null)
        {
            String action = statusTransitionDTO.getActionToExecute();
            processEvent = colonyService.getProcessEventByName(colony, action);
        }
        colony.setEvent(processEvent);
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
        List<TransitionEvaluation> transitionEvaluations =
            colonyService.evaluateNextTransitions(colony);
        return transitionMapper.toDtos(transitionEvaluations);
    }
}
