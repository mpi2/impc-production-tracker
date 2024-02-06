package org.gentar.biology.plan.engine;

import org.gentar.biology.status.StatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.gentar.mockdata.MockData.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


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