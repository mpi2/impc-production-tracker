package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.EntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhenotypingStageCommonMapperTest
{
    private PhenotypingStageCommonMapper testInstance;

    @Mock
    private TissueDistributionMapper tissueDistributionMapper;

    @BeforeEach
    void setUp()
    {
        testInstance = new PhenotypingStageCommonMapper(tissueDistributionMapper);
    }

    @Test
    void toDto()
    {

    }

    @Test
    void toEntity()
    {

    }
}