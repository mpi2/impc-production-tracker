package org.gentar.biology.specimen;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.strain.StrainMapper;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.gentar.statemachine.ProcessEvent;
import org.springframework.stereotype.Component;

@Component
public class SpecimenMapper implements Mapper<Specimen, SpecimenDTO>
{
    private EntityMapper entityMapper;
    private SpecimenService specimenService;
    private StrainMapper strainMapper;

    public SpecimenMapper(
        EntityMapper entityMapper, SpecimenService specimenService, StrainMapper strainMapper)
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
    //TODO: Why we are not showing the state machine for specimen?
    public Specimen toEntity(SpecimenDTO specimenDTO)
    {
        Specimen specimen = entityMapper.toTarget(specimenDTO, Specimen.class);
        setEvent(specimen, specimenDTO);
        specimen.setSpecimenType(specimenService.getSpecimenTypeByName(specimenDTO.getSpecimenTypeName()));
        specimen.setStrain(strainMapper.toEntity(specimenDTO.getStrainName()));
        return specimen;
    }

    private void setEvent(Specimen specimen, SpecimenDTO specimenDTO)
    {
        ProcessEvent processEvent = null;
        StatusTransitionDTO statusTransitionDTO = specimenDTO.getStatusTransitionDTO();
        if (statusTransitionDTO != null)
        {
            String action = statusTransitionDTO.getActionToExecute();
            processEvent = specimenService.getProcessEventByName(specimen, action);
        }
        specimen.setEvent(processEvent);
    }
}
