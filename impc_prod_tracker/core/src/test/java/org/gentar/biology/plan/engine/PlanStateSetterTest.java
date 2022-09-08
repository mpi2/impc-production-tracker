package org.gentar.biology.plan.engine;

import static org.gentar.mockdata.MockData.TEST_NAME;
import static org.gentar.mockdata.MockData.planMockData;
import static org.gentar.mockdata.MockData.statusMockData;
import static org.junit.jupiter.api.Assertions.*;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.status.StatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class PlanStateSetterTest {

    @Mock
    private StatusService statusService;

    PlanStateSetter testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new PlanStateSetter(statusService);
    }
    @Test
    void setStatus() {
        assertDoesNotThrow(() ->
            testInstance.setStatus(planMockData(),statusMockData())
        );
    }

    @Test
    void setStatusByName() {
        assertDoesNotThrow(() ->
            testInstance.setStatusByName(planMockData(),TEST_NAME)
        );
    }

    @Test
    void setInitialStatus() {
        assertDoesNotThrow(() ->
            testInstance.setInitialStatus(planMockData())
        );
    }
}