package org.gentar.biology.plan.mappers;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanBasicDataDTO;
import org.gentar.biology.plan.PlanUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanUpdateMapperTest
{
    private PlanUpdateMapper testInstance;

    @Mock
    private PlanBasicDataMapper planBasicDataMapper;

    @BeforeEach
    void setUp()
    {
        testInstance = new PlanUpdateMapper(planBasicDataMapper);
    }

    @Test
    void toDto()
    {
        Plan plan = new Plan();
        testInstance.toDto(plan);

        verify(planBasicDataMapper, times(1)).toDto(plan);
    }

    @Test
    void toEntity()
    {
        PlanUpdateDTO planUpdateDTO = new PlanUpdateDTO();
        planUpdateDTO.setId(1L);
        PlanBasicDataDTO planBasicDataDTO = new PlanBasicDataDTO();
        planUpdateDTO.setPlanBasicDataDTO(planBasicDataDTO);
        when(planBasicDataMapper.toEntity(planBasicDataDTO)).thenReturn(new Plan());

        Plan plan = testInstance.toEntity(planUpdateDTO);

        verify(planBasicDataMapper, times(1)).toEntity(planUpdateDTO.getPlanBasicDataDTO());
        assertThat(plan.getId(), is(planUpdateDTO.getId()));
    }
}
