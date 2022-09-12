package org.gentar.biology.plan.engine;

import static org.gentar.mockdata.MockData.phenotypingAttemptMockData;
import static org.gentar.mockdata.MockData.phenotypingStageMockData;
import static org.gentar.mockdata.MockData.planMockData;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import javax.persistence.EntityManager;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanStatusManager;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.project.engine.ProjectUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlanCreatorTest {

    @Mock
    private EntityManager entityManager;
    @Mock
    private HistoryService<Plan> historyService;
    @Mock
    private PlanStatusManager planStatusManager;
    @Mock
    private PlanValidator planValidator;
    @Mock
    private ProjectUpdater projectUpdater;

    PlanCreator testInstance;


    @BeforeEach
    void setUp() {
        testInstance = new PlanCreator(entityManager,
            historyService,
            planStatusManager,
            planValidator,
            projectUpdater);
    }

    @Test
    void createPlan() {

        Plan mockPlan = planMockData();
        PhenotypingAttempt phenotypingAttempt =phenotypingAttemptMockData();

        phenotypingAttempt.setPhenotypingStages(Set.of(phenotypingStageMockData()));

        mockPlan.setPhenotypingAttempt(phenotypingAttempt);

        Plan plan =
            testInstance.createPlan(mockPlan);
        assertEquals(plan.getPin(), "PIN:0000000001");
    }
}