package org.gentar.biology.specimen;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.status.StatusStampMapper;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.gentar.biology.strain.StrainMapper;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.gentar.common.state_machine.TransitionDTO;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.gentar.statemachine.TransitionMapper;
import org.springframework.stereotype.Component;
import java.util.Comparator;
import java.util.List;

@Component
public class SpecimenMapper implements Mapper<Specimen, SpecimenDTO>
{
    private final EntityMapper entityMapper;
    private final SpecimenService specimenService;
    private final StrainMapper strainMapper;
    private final StatusStampMapper statusStampMapper;
    private final TransitionMapper transitionMapper;
    private final SpecimenPropertyMapper specimenPropertyMapper;

    public SpecimenMapper(
        EntityMapper entityMapper,
        SpecimenService specimenService,
        StrainMapper strainMapper,
        StatusStampMapper statusStampMapper,
        TransitionMapper transitionMapper,
        SpecimenPropertyMapper specimenPropertyMapper)
    {
        this.entityMapper = entityMapper;
        this.specimenService = specimenService;
        this.strainMapper = strainMapper;
        this.statusStampMapper = statusStampMapper;
        this.transitionMapper = transitionMapper;
        this.specimenPropertyMapper = specimenPropertyMapper;
    }

    @Override
    public SpecimenDTO toDto(Specimen specimen)
    {
        SpecimenDTO specimenDTO = entityMapper.toTarget(specimen, SpecimenDTO.class);
        if (specimen != null)
        {
            specimenDTO.setStrainName(strainMapper.toDto(specimen.getStrain()));
            setStatusStampsDTOS(specimenDTO, specimen);
            addStatusTransitionDTO(specimenDTO, specimen);
            addSpecimenPropertiesDTO(specimenDTO, specimen);
        }
        return specimenDTO;
    }

    private void setStatusStampsDTOS(SpecimenDTO specimenDTO, Specimen specimen)
    {
        List<StatusStampsDTO> statusStampsDTOS =
            statusStampMapper.toDtos(specimen.getSpecimenStatusStamps());
        statusStampsDTOS.sort(Comparator.comparing(StatusStampsDTO::getDate));
        specimenDTO.setStatusStampsDTOS(statusStampsDTOS);
    }

    private void addStatusTransitionDTO(SpecimenDTO specimenDTO, Specimen specimen)
    {
        StatusTransitionDTO statusTransitionDTO = new StatusTransitionDTO();
        statusTransitionDTO.setCurrentStatus(specimen.getProcessDataStatus().getName());
        statusTransitionDTO.setTransitions(getTransitions(specimen));
        specimenDTO.setStatusTransitionDTO(statusTransitionDTO);
    }

    private List<TransitionDTO> getTransitions(Specimen specimen)
    {
        List<TransitionEvaluation> transitionEvaluations =
            specimenService.evaluateNextTransitions(specimen);
        return transitionMapper.toDtos(transitionEvaluations);
    }

    private void addSpecimenPropertiesDTO(SpecimenDTO specimenDTO, Specimen specimen)
    {
        List<SpecimenPropertyDTO> specimenPropertyDTOS =
            specimenPropertyMapper.toDtos(specimen.getSpecimenProperties());
        specimenDTO.setSpecimenPropertyDTOS(specimenPropertyDTOS);
    }

    @Override
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
        specimen.setProcessDataEvent(processEvent);
    }
}
