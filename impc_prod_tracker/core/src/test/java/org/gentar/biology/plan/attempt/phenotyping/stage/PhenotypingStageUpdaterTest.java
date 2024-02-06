package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.audit.history.HistoryService;
import org.gentar.biology.plan.engine.PlanUpdater;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.statemachine.StateTransitionsManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.gentar.mockdata.MockData.phenotypingStageMockData;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@ExtendWith(MockitoExtension.class)
class PhenotypingStageUpdaterTest {

    @Mock
    private HistoryService<PhenotypingStage> historyService;
    @Mock
    private StateTransitionsManager stateTransitionsManager;
    @Mock
    private PhenotypingStageRepository phenotypingStageRepository;
    @Mock
    private PlanUpdater planUpdater;
    @Mock
    private ContextAwarePolicyEnforcement policyEnforcement;
    @Mock
    private PhenotypingStageValidator phenotypingStageValidator;

    PhenotypingStageUpdater testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new PhenotypingStageUpdater(historyService,
            stateTransitionsManager,
            phenotypingStageRepository,
            planUpdater,
            policyEnforcement,
            phenotypingStageValidator);
    }

    @Test
    void update() {

        assertDoesNotThrow(() ->
        testInstance.update(phenotypingStageMockData(),phenotypingStageMockData())
        );
    }
}