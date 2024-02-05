package org.gentar.biology.outcome;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.ColonyDTO;
import org.gentar.biology.colony.mappers.ColonyMapper;
import org.gentar.biology.specimen.Specimen;
import org.gentar.biology.specimen.SpecimenDTO;
import org.gentar.biology.specimen.SpecimenMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OutcomeCommonMapperTest
{
    private OutcomeCommonMapper testInstance;

    @Mock
    private ColonyMapper colonyMapper;
    @Mock
    private SpecimenMapper specimenMapper;

    @BeforeEach
    void setUp()
    {
        testInstance = new OutcomeCommonMapper(colonyMapper, specimenMapper);
    }

    @Test
    void toDtoColony()
    {
        Outcome outcome = new Outcome();
        Colony colony = new Colony();
        outcome.setColony(colony);

        testInstance.toDto(outcome);

        verify(colonyMapper, times(1)).toDto(colony);
        verify(specimenMapper, times(0)).toDto(any(Specimen.class));
    }

    @Test
    void toDtoSpecimen()
    {
        Outcome outcome = new Outcome();
        Specimen specimen = new Specimen();
        outcome.setSpecimen(specimen);

        testInstance.toDto(outcome);

        verify(specimenMapper, times(1)).toDto(specimen);
        verify(colonyMapper, times(0)).toDto(any(Colony.class));
    }

    @Test
    void toEntityColony()
    {
        OutcomeCommonDTO outcomeCommonDTO = new OutcomeCommonDTO();
        ColonyDTO colonyDTO = new ColonyDTO();
        outcomeCommonDTO.setColonyDTO(colonyDTO);
        when(colonyMapper.toEntity(colonyDTO)).thenReturn(new Colony());

        testInstance.toEntity(outcomeCommonDTO);

        verify(colonyMapper, times(1)).toEntity(colonyDTO);
        verify(specimenMapper, times(0)).toEntity(any(SpecimenDTO.class));
    }

    @Test
    void toEntitySpecimen()
    {
        OutcomeCommonDTO outcomeCommonDTO = new OutcomeCommonDTO();
        SpecimenDTO specimenDTO = new SpecimenDTO();
        outcomeCommonDTO.setSpecimenDTO(specimenDTO);
        when(specimenMapper.toEntity(specimenDTO)).thenReturn(new Specimen());

        testInstance.toEntity(outcomeCommonDTO);

        verify(specimenMapper, times(1)).toEntity(specimenDTO);
        verify(colonyMapper, times(0)).toEntity(any(ColonyDTO.class));
    }
}
