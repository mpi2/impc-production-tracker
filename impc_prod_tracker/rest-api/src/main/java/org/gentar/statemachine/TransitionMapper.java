package org.gentar.statemachine;

import org.gentar.Mapper;
import org.gentar.common.state_machine.TransitionDTO;
import org.springframework.stereotype.Component;

@Component
public class TransitionMapper implements Mapper<TransitionEvaluation, TransitionDTO>
{
    @Override
    public TransitionDTO toDto(TransitionEvaluation entity)
    {
        TransitionDTO transitionDTO = new TransitionDTO();
        transitionDTO.setAction(entity.getTransition().getName());
        transitionDTO.setDescription(entity.getTransition().getDescription());
        transitionDTO.setTriggeredByUser(entity.getTransition().isTriggeredByUser());
        transitionDTO.setAvailable(entity.isExecutable());
        transitionDTO.setNextStatus(entity.getTransition().getEndState().getName());
        transitionDTO.setNote(
            entity.getNote() == null ? entity.getTransition().getTriggerNote() : entity.getNote());
        return transitionDTO;
    }

    @Override
    public TransitionEvaluation toEntity(TransitionDTO dto)
    {
        return null;
    }
}
