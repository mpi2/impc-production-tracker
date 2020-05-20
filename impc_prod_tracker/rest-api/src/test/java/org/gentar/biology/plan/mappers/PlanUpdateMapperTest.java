package org.gentar.biology.plan.mappers;

import org.gentar.EntityMapper;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanService;
import org.gentar.biology.plan.PlanUpdateDTO;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PlanUpdateMapperTest
{
    private PlanUpdateMapper testInstance;

    @Mock
    private PlanBasicDataMapper planBasicDataMapper;

    @Mock
    private PlanService planService;

    @BeforeEach
    void setUp()
    {
        testInstance = new PlanUpdateMapper(planBasicDataMapper, planService);
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
        StatusTransitionDTO statusTransitionDTO = new StatusTransitionDTO();
        statusTransitionDTO.setActionToExecute("abandonWhenCreated");
        planUpdateDTO.setStatusTransitionDTO(statusTransitionDTO);
        Plan plan = new Plan();
        when(planBasicDataMapper.toEntity(any())).thenReturn(plan);

        testInstance.toEntity(planUpdateDTO);

        verify(planService, times(1)).getProcessEventByName(plan, "abandonWhenCreated");
    }
}