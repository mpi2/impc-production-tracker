package org.gentar.biology.outcome;

import org.gentar.biology.outcome.type.OutcomeType;
import org.gentar.biology.outcome.type.OutcomeTypeName;
import org.gentar.biology.plan.Plan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OutcomeResponseMapperTest
{
    private OutcomeResponseMapper testInstance;

    @Mock
    private OutcomeCommonMapper outcomeCommonMapper;

    @BeforeEach
    void setUp()
    {
        testInstance = new OutcomeResponseMapper(outcomeCommonMapper);
    }

    @Test
    void toDto()
    {
        Outcome outcome = new Outcome();
        OutcomeType outcomeType = new OutcomeType();
        outcomeType.setName(OutcomeTypeName.COLONY.getLabel());
        outcome.setOutcomeType(outcomeType);
        outcome.setId(1L);
        outcome.setTpo("tpo1");
        Plan plan = new Plan();
        plan.setPin("pin1");
        outcome.setPlan(plan);

        OutcomeResponseDTO outcomeResponseDTO = testInstance.toDto(outcome);

        verify(outcomeCommonMapper, times(1)).toDto(outcome);
        assertThat(outcomeResponseDTO.getPin(), is("pin1"));
        assertThat(outcomeResponseDTO.getTpo(), is("tpo1"));
        assertThat(outcomeResponseDTO.getOutcomeTypeName(), is(OutcomeTypeName.COLONY.getLabel()));
    }
}
