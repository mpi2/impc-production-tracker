package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.common.state_machine.StatusTransitionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhenotypingStageUpdateMapperTest
{
    private PhenotypingStageUpdateMapper testInstance;

    @Mock
    private PhenotypingStageCommonMapper phenotypingStageCommonMapper;

    @Mock
    private PhenotypingStageService phenotypingStageService;

    @BeforeEach
    void setUp()
    {
        testInstance = new PhenotypingStageUpdateMapper(phenotypingStageCommonMapper, phenotypingStageService);
    }

    @Test
    void toEntity()
    {
        PhenotypingStageUpdateDTO phenotypingStageUpdateDTO = new PhenotypingStageUpdateDTO();
        phenotypingStageUpdateDTO.setId(1L);
        phenotypingStageUpdateDTO.setPsn("psn");
        StatusTransitionDTO statusTransitionDTO = new StatusTransitionDTO();
        statusTransitionDTO.setActionToExecute("action");
        phenotypingStageUpdateDTO.setStatusTransitionDTO(statusTransitionDTO);

        PhenotypingStageCommonDTO phenotypingStageCommonDTO = new PhenotypingStageCommonDTO();
        phenotypingStageUpdateDTO.setPhenotypingStageCommonDTO(phenotypingStageCommonDTO);
        when(phenotypingStageCommonMapper.toEntity(phenotypingStageCommonDTO)).thenReturn(new PhenotypingStage());

        PhenotypingStage phenotypingStage = testInstance.toEntity(phenotypingStageUpdateDTO);

        verify(phenotypingStageCommonMapper, times(1)).toEntity(
                phenotypingStageUpdateDTO.getPhenotypingStageCommonDTO());
        verify(phenotypingStageService, times(1)).getProcessEventByName(phenotypingStage,
                phenotypingStageUpdateDTO.getStatusTransitionDTO().getActionToExecute());
        assertThat(phenotypingStage.getId(), is(phenotypingStageUpdateDTO.getId()));
        assertThat(phenotypingStage.getPsn(), is(phenotypingStageUpdateDTO.getPsn()));
    }
}