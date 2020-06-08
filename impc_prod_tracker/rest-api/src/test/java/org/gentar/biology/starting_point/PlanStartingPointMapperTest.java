package org.gentar.biology.starting_point;

import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.outcome.OutcomeService;
import org.gentar.biology.plan.plan_starting_point.PlanStartingPointDTO;
import org.gentar.biology.plan.starting_point.PlanStartingPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlanStartingPointMapperTest
{
    private PlanStartingPointMapper testInstance;

    @Mock
    private OutcomeService outcomeService;

    @BeforeEach
    void setUp()
    {
        testInstance = new PlanStartingPointMapper(outcomeService);
    }

    @Test
    void toDto()
    {
        PlanStartingPoint planStartingPoint = new PlanStartingPoint();
        planStartingPoint.setId(1L);
        Outcome outcome = new Outcome();
        outcome.setTpo("tpo");
        planStartingPoint.setOutcome(outcome);

        PlanStartingPointDTO planStartingPointDTO = testInstance.toDto(planStartingPoint);

        assertThat(planStartingPointDTO.getId(), is(1L));
        assertThat(planStartingPointDTO.getTpo(), is("tpo"));
    }

    @Test
    void toEntity()
    {
        PlanStartingPointDTO planStartingPointDTO = new PlanStartingPointDTO();
        planStartingPointDTO.setId(1L);
        planStartingPointDTO.setTpo("tpo");
        Outcome outcome = new Outcome();
        outcome.setTpo("tpo");
        when(outcomeService.getByTpoFailsIfNotFound("tpo")).thenReturn(outcome);

        PlanStartingPoint planStartingPoint = testInstance.toEntity(planStartingPointDTO);

        assertThat(planStartingPoint.getId(), is(1L));
        assertThat(planStartingPoint.getOutcome().getTpo(), is("tpo"));
    }
}