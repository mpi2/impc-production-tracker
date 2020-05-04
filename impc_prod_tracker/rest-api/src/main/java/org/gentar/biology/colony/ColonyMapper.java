package org.gentar.biology.colony;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.strain.StrainMapper;
import org.gentar.biology.status.StatusService;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.gentar.common.state_machine.TransitionDTO;
import org.gentar.statemachine.TransitionEvaluation;
import org.gentar.statemachine.TransitionMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ColonyMapper implements Mapper<Colony, ColonyDTO>
{
    private EntityMapper entityMapper;
    private StatusService statusService;
    private StrainMapper strainMapper;
    private ColonyService colonyService;
    private TransitionMapper transitionMapper;

    public ColonyMapper(
        EntityMapper entityMapper,
        StatusService statusService,
        StrainMapper strainMapper,
        ColonyService colonyService,
        TransitionMapper transitionMapper)
    {
        this.entityMapper = entityMapper;
        this.statusService = statusService;
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
    public Colony toEntity(ColonyDTO dto)
    {
        Colony colony = entityMapper.toTarget(dto, Colony.class);
        colony.setStrain(strainMapper.toEntity(dto.getStrainName()));
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
        List<TransitionEvaluation> transitionEvaluations =
            colonyService.evaluateNextTransitions(colony);
        return transitionMapper.toDtos(transitionEvaluations);
    }
}
