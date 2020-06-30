package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhenotypingStageCreationMapperTest {

    private PhenotypingStageCreationMapper testInstance;

    @Mock
    private PhenotypingStageCommonMapper phenotypingStageCommonMapper;

    @Mock
    private PhenotypingStageService phenotypingStageService;

    @BeforeEach
    void setUp()
    {
        testInstance = new PhenotypingStageCreationMapper(phenotypingStageCommonMapper, phenotypingStageService);
    }

    @Test
    void toEntity()
    {
        PhenotypingStageCreationDTO phenotypingStageCreationDTO = new PhenotypingStageCreationDTO();
        phenotypingStageCreationDTO.setPhenotypingTypeName("stageTypeName");
        PhenotypingStageCommonDTO phenotypingStageCommonDTO = new PhenotypingStageCommonDTO();
        phenotypingStageCreationDTO.setPhenotypingStageCommonDTO(phenotypingStageCommonDTO);
        when(phenotypingStageCommonMapper.toEntity(phenotypingStageCommonDTO)).thenReturn(new PhenotypingStage());

        testInstance.toEntity(phenotypingStageCreationDTO);

        verify(phenotypingStageCommonMapper, times(1)).toEntity(
                phenotypingStageCreationDTO.getPhenotypingStageCommonDTO());
        verify(phenotypingStageService, times(1)).getPhenotypingStageTypeByNameFailingWhenNull(
                phenotypingStageCreationDTO.getPhenotypingTypeName());
    }
}