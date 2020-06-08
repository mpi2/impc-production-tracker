package org.gentar.biology.outcome;

import org.gentar.biology.mutation.MutationMapper;
import org.gentar.biology.outcome.type.OutcomeType;
import org.gentar.biology.outcome.type.OutcomeTypeName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    @Mock
    private MutationMapper mutationMapper;

    @BeforeEach
    void setUp()
    {
        testInstance = new OutcomeCreationMapper(outcomeCommonMapper, outcomeService, mutationMapper);
    }

    @Test
    void toEntityColony()
    {
        OutcomeCreationDTO outcomeCreationDTO = new OutcomeCreationDTO();
        outcomeCreationDTO.setOutcomeTypeName(OutcomeTypeName.COLONY.getLabel());
        testInstance.toEntity(outcomeCreationDTO);

        verify(outcomeCommonMapper, times(1)).toEntity(outcomeCreationDTO.getOutcomeCommonDTO());
        verify(mutationMapper, times(1)).toEntities(outcomeCreationDTO.getMutationDTOS());
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
        verify(mutationMapper, times(1)).toEntities(outcomeCreationDTO.getMutationDTOS());
        verify(outcomeService, times(1))
            .getOutcomeTypeByNameFailingWhenNull(OutcomeTypeName.SPECIMEN.getLabel());
    }
}