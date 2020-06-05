package org.gentar.biology.outcome;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OutcomeUpdateMapperTest
{
    private OutcomeUpdateMapper testInstance;

    @Mock
    private OutcomeCommonMapper outcomeCommonMapper;

    @BeforeEach
    void setUp()
    {
        testInstance = new OutcomeUpdateMapper(outcomeCommonMapper);
    }

    @Test
    void toDto()
    {
        Outcome outcome = new Outcome();
        testInstance.toDto(outcome);

        verify(outcomeCommonMapper, times(1)).toDto(outcome);
    }

    @Test
    void toEntity()
    {
        OutcomeUpdateDTO outcomeUpdateDTO = new OutcomeUpdateDTO();
        OutcomeCommonDTO outcomeCommonDTO = new OutcomeCommonDTO();
        outcomeUpdateDTO.setOutcomeCommonDTO(outcomeCommonDTO);
        testInstance.toEntity(outcomeUpdateDTO);

        verify(outcomeCommonMapper, times(1)).toEntity(outcomeCommonDTO);
    }
}