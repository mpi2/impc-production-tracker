package org.gentar.biology.plan.engine;

import static org.gentar.mockdata.MockData.PIN_000000001;
import static org.gentar.mockdata.MockData.planMockData;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanRepository;
import org.gentar.biology.plan.PlanStatusManager;
import org.gentar.biology.plan.attempt.crispr.guide.GuideRepository;
import org.gentar.biology.project.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlanUpdaterImplTest {

    @Mock
    private HistoryService<Plan> historyService;
    @Mock
    private PlanRepository planRepository;
    @Mock
    private GuideRepository guideRepository;
    @Mock
    private PlanValidator planValidator;
    @Mock
    private ProjectService projectService;
    @Mock
    private PlanStatusManager planStatusManager;

    PlanUpdaterImpl testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new PlanUpdaterImpl(historyService,
                planRepository,
                guideRepository, planValidator,
                projectService,
                planStatusManager);
    }

    @Test
    void updatePlan() {

        lenient().when(planRepository.findPlanByPin(PIN_000000001)).thenReturn(planMockData());
        History exception = assertDoesNotThrow(() ->
                testInstance.updatePlan(planMockData(), planMockData())
        );

        assertNull(exception);
    }

    @Test
    void notifyChangeInChild() {
    }
}