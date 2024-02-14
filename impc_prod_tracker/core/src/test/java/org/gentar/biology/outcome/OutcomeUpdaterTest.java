package org.gentar.biology.outcome;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.plan.engine.PlanUpdater;
import org.gentar.statemachine.StateTransitionsManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.gentar.mockdata.MockData.outcomeMockData;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class OutcomeUpdaterTest {

    @Mock
    private HistoryService<Outcome> historyService;
    @Mock
    private StateTransitionsManager stateTransitionsManager;
    @Mock
    private OutcomeRepository outcomeRepository;
    @Mock
    private PlanUpdater planUpdater;
    @Mock
    private OutcomeValidator outcomeValidator;

    OutcomeUpdater testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new OutcomeUpdater(historyService,
            stateTransitionsManager,
            outcomeRepository,
            planUpdater,
            outcomeValidator);
    }

    @Test
    void update() {

        History exception = assertDoesNotThrow(() ->
            testInstance.update(outcomeMockData(),outcomeMockData())
        );

        assertNull(exception);
    }
}