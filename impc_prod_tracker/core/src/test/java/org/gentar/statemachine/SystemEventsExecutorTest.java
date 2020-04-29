package org.gentar.statemachine;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.AttemptTypes;
import org.gentar.biology.plan.engine.PlanStateMachineResolver;
import org.gentar.biology.plan.engine.crispr.CrisprProductionPlanEvent;
import org.gentar.biology.plan.engine.crispr.CrisprProductionPlanState;
import org.gentar.exceptions.SystemOperationFailedException;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.test_util.PlanBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SystemEventsExecutorTest
{
    private SystemEventsExecutor testInstance;
    private PlanStateMachineResolver planStateMachineResolver = new PlanStateMachineResolver();

    @Mock
    private StateTransitionsManager stateTransitionsManager;

    @BeforeEach
    public void setup()
    {
        testInstance = new SystemEventsExecutor(stateTransitionsManager);
    }

    @Test
    public void testWhenOnlyOneTransition()
    {
        Plan planCreated = PlanBuilder.getInstance()
            .withStatus(CrisprProductionPlanState.PlanCreated.getInternalName())
            .withAttemptType(AttemptTypes.CRISPR.getTypeName())
            .build();
        Plan planWithAttemptInProgress = PlanBuilder.getInstance()
            .withStatus(CrisprProductionPlanState.AttemptInProgress.getInternalName())
            .withAttemptType(AttemptTypes.CRISPR.getTypeName())
            .build();

        testInstance.setStateMachineResolver(planStateMachineResolver);
        when(stateTransitionsManager.processEvent(any(Plan.class)))
            .thenReturn(planWithAttemptInProgress);

        testInstance.execute(planCreated);

        verify(stateTransitionsManager, times(2)).processEvent(any(Plan.class));
        assertThat(
            "Not expected status",
            planCreated.getStatus().getName(),
            is(CrisprProductionPlanState.AttemptInProgress.getInternalName()));
    }

    @Test
    public void testWhenMoreThanTwoTransition()
    {
        Plan planCreated = PlanBuilder.getInstance()
            .withStatus(CrisprProductionPlanState.PlanCreated.getInternalName())
            .withAttemptType(AttemptTypes.CRISPR.getTypeName())
            .build();
        Plan planWithAttemptInProgress = PlanBuilder.getInstance()
            .withStatus(CrisprProductionPlanState.AttemptInProgress.getInternalName())
            .withAttemptType(AttemptTypes.CRISPR.getTypeName())
            .build();
        Plan planWithEmbryosObtained = PlanBuilder.getInstance()
            .withStatus(CrisprProductionPlanState.EmbryosObtained.getInternalName())
            .withAttemptType(AttemptTypes.CRISPR.getTypeName())
            .build();

        PlanStateMachineResolver planStateMachineResolver = new PlanStateMachineResolver();
        testInstance.setStateMachineResolver(planStateMachineResolver);
        when(stateTransitionsManager.processEvent(any(Plan.class)))
            .thenReturn(planWithAttemptInProgress, planWithEmbryosObtained);

        testInstance.execute(planCreated);

        verify(stateTransitionsManager, times(3)).processEvent(any(Plan.class));
        assertThat(
            "Not expected status",
            planCreated.getStatus().getName(),
            is(CrisprProductionPlanState.EmbryosObtained.getInternalName()));
    }

    @Test
    public void testExceptionWhenStateTransitionsManagerNull()
    {
        SystemOperationFailedException thrown = assertThrows(SystemOperationFailedException.class,
            () -> testInstance.execute(new Plan()),
            "Exception not thrown");
        assertThat(
            "Not expected message",
            thrown.getDebugMessage(),
            is("System triggered transitions could not be executed"));
    }

    @Test
    public void testExceptionWhenUserTransitionPresent()
    {
        Plan planCreated = PlanBuilder.getInstance()
            .withStatus(CrisprProductionPlanState.PlanCreated.getInternalName())
            .withAttemptType(AttemptTypes.CRISPR.getTypeName())
            .build();
        Plan planWithAttemptInProgress = PlanBuilder.getInstance()
            .withStatus(CrisprProductionPlanState.AttemptInProgress.getInternalName())
            .withAttemptType(AttemptTypes.CRISPR.getTypeName())
            .build();

        testInstance.setStateMachineResolver(planStateMachineResolver);
        when(stateTransitionsManager.processEvent(any(Plan.class)))
            .thenReturn(planWithAttemptInProgress);

        planCreated.setEvent(CrisprProductionPlanEvent.abandonWhenCreated);

        UserOperationFailedException thrown = assertThrows(UserOperationFailedException.class,
            () -> testInstance.execute(planCreated),
            "Exception not thrown");
        assertThat(
            "Not expected message",
            thrown.getMessage(),
            is("Trying to execute action [abandonWhenCreated] but also modifying data that causes a change in status. " +
                "Please do this in two different steps."));
    }
}