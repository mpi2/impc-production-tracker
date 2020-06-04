package org.gentar.biology.outcome;

import org.gentar.Mapper;
import org.gentar.biology.colony.ColonyMapper;
import org.gentar.biology.specimen.SpecimenMapper;
import org.springframework.stereotype.Component;

@Component
public class OutcomeCommonMapper implements Mapper<Outcome, OutcomeCommonDTO>
{
    private ColonyMapper colonyMapper;
    private SpecimenMapper specimenMapper;

    public OutcomeCommonMapper(ColonyMapper colonyMapper, SpecimenMapper specimenMapper)
    {
        this.colonyMapper = colonyMapper;
        this.specimenMapper = specimenMapper;
    }

    @Override
    public OutcomeCommonDTO toDto(Outcome outcome)
    {
        OutcomeCommonDTO outcomeCommonDTO = new OutcomeCommonDTO();
        setColonyOrSpecimenDto(outcomeCommonDTO, outcome);
        return outcomeCommonDTO;
    }

    private void setColonyOrSpecimenDto(OutcomeCommonDTO outcomeCommonDTO, Outcome outcome)
    {
        if (outcome.getColony() != null)
        {
            outcomeCommonDTO.setColonyDTO(colonyMapper.toDto(outcome.getColony()));
        }
        else if (outcome.getSpecimen() != null)
        {
            outcomeCommonDTO.setSpecimenDTO(specimenMapper.toDto(outcome.getSpecimen()));
        }
    }

    @Override
    public Outcome toEntity(OutcomeCommonDTO outcomeCommonDTO)
    {
        Outcome outcome = new Outcome();
        setColonyOrSpecimenEntity(outcome, outcomeCommonDTO);
        return outcome;
    }

    private void setColonyOrSpecimenEntity(Outcome outcome, OutcomeCommonDTO outcomeCommonDTO)
    {
        if (outcomeCommonDTO.getColonyDTO() != null)
        {
            outcome.setColony(colonyMapper.toEntity(outcomeCommonDTO.getColonyDTO()));
        }
        else if (outcomeCommonDTO.getSpecimenDTO() != null)
        {
            outcome.setSpecimen(specimenMapper.toEntity(outcomeCommonDTO.getSpecimenDTO()));
        }
    }
}
