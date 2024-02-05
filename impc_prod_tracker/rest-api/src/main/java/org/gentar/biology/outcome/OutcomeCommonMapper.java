package org.gentar.biology.outcome;

import org.gentar.Mapper;
import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.mappers.ColonyMapper;
import org.gentar.biology.specimen.Specimen;
import org.gentar.biology.specimen.SpecimenMapper;
import org.springframework.stereotype.Component;

@Component
public class OutcomeCommonMapper implements Mapper<Outcome, OutcomeCommonDTO>
{
    private final ColonyMapper colonyMapper;
    private final SpecimenMapper specimenMapper;

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
            Colony colony = colonyMapper.toEntity(outcomeCommonDTO.getColonyDTO());
            colony.setId(outcome.getId());
            colony.setOutcome(outcome);
            outcome.setColony(colony);
        }
        else if (outcomeCommonDTO.getSpecimenDTO() != null)
        {
            Specimen specimen = specimenMapper.toEntity(outcomeCommonDTO.getSpecimenDTO());
            specimen.setOutcome(outcome);
            outcome.setSpecimen(specimen);
        }
    }
}
