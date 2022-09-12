package org.gentar.biology.plan.attempt.phenotyping.stage;

import static org.gentar.mockdata.MockData.phenotypingStageMockData;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.gentar.biology.status.Status;
import org.gentar.statemachine.ProcessEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhenotypingStageStateMachineResolverTest {


    PhenotypingStageStateMachineResolver testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new PhenotypingStageStateMachineResolver();
    }

    @Test
    void getProcessEventByActionName() {
       ProcessEvent processEvent = testInstance.getProcessEventByActionName(phenotypingStageMockData(),"updateToRederivationStarted");

       assertEquals(processEvent.getName(),"updateToRederivationStarted");
    }

    @Test
    void getAvailableTransitionsByEntityStatus() {

        PhenotypingStage phenotypingStage = phenotypingStageMockData();
        Status status = new Status();
        status.setIsAbortionStatus(true);
        status.setName("Phenotyping Registered");
        phenotypingStage.setProcessDataStatus(status);

        List<ProcessEvent> processEvents= testInstance.getAvailableTransitionsByEntityStatus(phenotypingStage);

        assertEquals(processEvents.size(),3);
    }
}