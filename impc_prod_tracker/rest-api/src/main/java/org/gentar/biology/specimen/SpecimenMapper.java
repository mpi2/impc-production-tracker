package org.gentar.biology.specimen;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.specimen.engine.SpecimenEvent;
import org.gentar.biology.specimen.engine.SpecimenState;
import org.gentar.biology.strain.StrainMapper;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.gentar.common.state_machine.TransitionDTO;
import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SpecimenMapper implements Mapper<Specimen, SpecimenDTO>
{
    private EntityMapper entityMapper;
    private SpecimenService specimenService;
    private StrainMapper strainMapper;

    public SpecimenMapper(EntityMapper entityMapper, SpecimenService specimenService, StrainMapper strainMapper)
    {
        this.entityMapper = entityMapper;
        this.specimenService = specimenService;
        this.strainMapper = strainMapper;
    }

    @Override
    public SpecimenDTO toDto(Specimen entity)
    {
        SpecimenDTO specimenDTO = entityMapper.toTarget(entity, SpecimenDTO.class);
        if (entity != null) {
            specimenDTO.setStrainName(strainMapper.toDto(entity.getStrain()));
        }
        return specimenDTO;
    }

    @Override
    public Specimen toEntity(SpecimenDTO dto)
    {
        Specimen specimen = entityMapper.toTarget(dto, Specimen.class);
        specimen.setSpecimenType(specimenService.getSpecimenTypeByName(dto.getSpecimenTypeName()));
        specimen.setStrain(strainMapper.toEntity(dto.getStrainName()));
        return specimen;
    }

    private StatusTransitionDTO buildStatusTransitionDTO(Specimen specimen)
    {
        StatusTransitionDTO statusTransitionDTO = new StatusTransitionDTO();
        statusTransitionDTO.setCurrentStatus(specimen.getStatus().getName());
        statusTransitionDTO.setTransitions(getTransitions(specimen));
        return statusTransitionDTO;
    }

    private List<TransitionDTO> getTransitions(Specimen specimen)
    {
        List<TransitionDTO> transitionDTOS = new ArrayList<>();
        String currentStatusName = specimen.getStatus().getName();
        ProcessState processState = SpecimenState.getStateByInternalName(currentStatusName);
        if (processState != null)
        {
            List<ProcessEvent> specimenEvents =
                    EnumStateHelper.getAvailableEventsByState(SpecimenEvent.getAllEvents(), processState);
            specimenEvents.forEach(x -> {
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
