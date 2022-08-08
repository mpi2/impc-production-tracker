package org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors;

import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStageStateSetter;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.PhenotypingStageEvent;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.PhenotypingStageState;
import org.gentar.biology.status.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PhenotypingStageProcessorWithoutValidationsTest
{
    private PhenotypingStageProcessorWithoutValidations testInstance;

    @Mock
    private PhenotypingStageStateSetter stageStateSetter;

    @BeforeEach
    void setUp()
    {
        testInstance = new PhenotypingStageProcessorWithoutValidations(stageStateSetter);
    }

    @Test
    public void testProcess()
    {
        PhenotypingStage phenotypingStage = buildPhenotypingStage(
            PhenotypingStageState.PhenotypingRegistered.getInternalName());
        phenotypingStage.setEvent(PhenotypingStageEvent.updateToPhenotypingStarted);

        testInstance.process(phenotypingStage);

        verify(
            stageStateSetter, times(1)).setStatusByName(
            any(PhenotypingStage.class), any(String.class));
    }

    private PhenotypingStage buildPhenotypingStage(String statusName)
    {
        PhenotypingStage phenotypingStage = new PhenotypingStage();
        Status status = new Status();
        status.setName(statusName);
        phenotypingStage.setProcessDataStatus(status);
        return phenotypingStage;
    }
}