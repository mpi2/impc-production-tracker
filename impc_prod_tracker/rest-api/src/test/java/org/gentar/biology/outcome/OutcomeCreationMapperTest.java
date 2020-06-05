package org.gentar.biology.outcome;

import org.gentar.biology.outcome.type.OutcomeType;
import org.gentar.biology.outcome.type.OutcomeTypeName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OutcomeCreationMapperTest
{
    private OutcomeCreationMapper testInstance;

    @Mock
    private OutcomeCommonMapper outcomeCommonMapper;
    @Mock
    private OutcomeService outcomeService;

    @BeforeEach
    void setUp()
    {
        testInstance = new OutcomeCreationMapper(outcomeCommonMapper, outcomeService);
    }

    @Test
    void toDto()
    {
        Outcome outcome = new Outcome();
        OutcomeType outcomeType = new OutcomeType();
        outcome.setOutcomeType(outcomeType);
        testInstance.toDto(outcome);

        verify(outcomeCommonMapper, times(1)).toDto(outcome);
    }

    @Test
    void toEntityColony()
    {
        OutcomeCreationDTO outcomeCreationDTO = new OutcomeCreationDTO();
        outcomeCreationDTO.setOutcomeTypeName(OutcomeTypeName.COLONY.getLabel());
        testInstance.toEntity(outcomeCreationDTO);

        verify(outcomeCommonMapper, times(1)).toEntity(outcomeCreationDTO.getOutcomeCommonDTO());
        verify(outcomeService, times(1))
            .getOutcomeTypeByNameFailingWhenNull(OutcomeTypeName.COLONY.getLabel());
    }

    @Test
    void toEntitySpecimen()
    {
        OutcomeCreationDTO outcomeCreationDTO = new OutcomeCreationDTO();
        outcomeCreationDTO.setOutcomeTypeName(OutcomeTypeName.SPECIMEN.getLabel());
        testInstance.toEntity(outcomeCreationDTO);

        verify(outcomeCommonMapper, times(1)).toEntity(outcomeCreationDTO.getOutcomeCommonDTO());
        verify(outcomeService, times(1))
            .getOutcomeTypeByNameFailingWhenNull(OutcomeTypeName.SPECIMEN.getLabel());
    }
}