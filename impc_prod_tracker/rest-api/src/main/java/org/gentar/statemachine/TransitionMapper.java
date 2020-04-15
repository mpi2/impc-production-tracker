package org.gentar.statemachine;

import org.gentar.Mapper;
import org.gentar.common.state_machine.TransitionDTO;
import org.springframework.stereotype.Component;

@Component
public class TransitionMapper implements Mapper<ProcessEvent, TransitionDTO>
{
    @Override
    public TransitionDTO toDto(ProcessEvent entity)
    {
        TransitionDTO transitionDTO = new TransitionDTO();
        transitionDTO.setAction(entity.getName());
        transitionDTO.setDescription(entity.getDescription());
        transitionDTO.setAvailable(entity.isTriggeredByUser());
        transitionDTO.setNextStatus(entity.getEndState().getName());
        transitionDTO.setNote(entity.getTriggerNote());
        return transitionDTO;
    }

    @Override
    public ProcessEvent toEntity(TransitionDTO dto)
    {
        return null;
    }
}
