package org.gentar.biology.outcome;

import org.gentar.biology.mutation.MutationCreationDTO;
import org.gentar.biology.mutation.MutationCreationMapper;
import org.gentar.biology.outcome.type.OutcomeTypeName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;

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
    private MutationCreationMapper mutationCreationMapper;

    @BeforeEach
    void setUp()
    {
        testInstance = new OutcomeCreationMapper(outcomeCommonMapper, outcomeService, mutationCreationMapper);
    }

    @Test
    void toEntityColony()
    {
        OutcomeCreationDTO outcomeCreationDTO = new OutcomeCreationDTO();
        outcomeCreationDTO.setOutcomeTypeName(OutcomeTypeName.COLONY.getLabel());
        List<MutationCreationDTO> mutationCreationDTOS = new ArrayList<>();
        outcomeCreationDTO.setMutationCreationDTOS(mutationCreationDTOS);

        testInstance.toEntity(outcomeCreationDTO);

        verify(outcomeCommonMapper, times(1)).toEntity(outcomeCreationDTO.getOutcomeCommonDTO());
        verify(mutationCreationMapper, times(1)).toEntities(outcomeCreationDTO.getMutationCreationDTOS());
        verify(outcomeService, times(1))
            .getOutcomeTypeByNameFailingWhenNull(OutcomeTypeName.COLONY.getLabel());
    }

    @Test
    void toEntitySpecimen()
    {
        OutcomeCreationDTO outcomeCreationDTO = new OutcomeCreationDTO();
        outcomeCreationDTO.setOutcomeTypeName(OutcomeTypeName.SPECIMEN.getLabel());
        List<MutationCreationDTO> mutationCreationDTOS = new ArrayList<>();
        outcomeCreationDTO.setMutationCreationDTOS(mutationCreationDTOS);

        testInstance.toEntity(outcomeCreationDTO);

        verify(outcomeCommonMapper, times(1)).toEntity(outcomeCreationDTO.getOutcomeCommonDTO());
        verify(mutationCreationMapper, times(1)).toEntities(outcomeCreationDTO.getMutationCreationDTOS());
        verify(outcomeService, times(1))
            .getOutcomeTypeByNameFailingWhenNull(OutcomeTypeName.SPECIMEN.getLabel());
    }
}