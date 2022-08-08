package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.attempt.phenotyping.stage.status_stamp.PhenotypingStageStatusStamp;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageType;
import org.gentar.biology.status.Status;
import org.gentar.statemachine.TransitionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PhenotypingStageResponseMapperTest
{
    private PhenotypingStageResponseMapper testInstance;

    @Mock
    private PhenotypingStageCommonMapper phenotypingStageCommonMapper;

    @Mock
    private PhenotypingStageService phenotypingStageService;

    @Mock
    private TransitionMapper transitionMapper;

    @BeforeEach
    void setUp()
    {
        testInstance = new PhenotypingStageResponseMapper(phenotypingStageCommonMapper,
            phenotypingStageService, transitionMapper);
    }

    @Test
    void toDto()
    {
        PhenotypingStage phenotypingStage = new PhenotypingStage();
        phenotypingStage.setId(1L);
        phenotypingStage.setPsn("psn");
        Status status = new Status();
        status.setName("statusName");
        phenotypingStage.setProcessDataStatus(status);
        PhenotypingStageType phenotypingStageType = new PhenotypingStageType();
        phenotypingStageType.setName("typeName");
        phenotypingStage.setPhenotypingStageType(phenotypingStageType);

        Set<PhenotypingStageStatusStamp> statusStamps = new HashSet<>();
        PhenotypingStageStatusStamp phenotypingStageStatusStamp = new PhenotypingStageStatusStamp();
        phenotypingStageStatusStamp.setStatus(status);
        phenotypingStageStatusStamp.setPhenotypingStage(phenotypingStage);
        statusStamps.add(phenotypingStageStatusStamp);
        phenotypingStage.setPhenotypingStageStatusStamps(statusStamps);

        PhenotypingAttempt phenotypingAttempt = new PhenotypingAttempt();
        phenotypingAttempt.setPhenotypingExternalRef("externalRef");
        phenotypingAttempt.setPlan(new Plan());
        phenotypingStage.setPhenotypingAttempt(phenotypingAttempt);

        PhenotypingStageResponseDTO phenotypingStageResponseDTO = testInstance.toDto(phenotypingStage);

        verify(phenotypingStageCommonMapper, times(1)).toDto(phenotypingStage);
        verify(phenotypingStageService, times(1)).evaluateNextTransitions(phenotypingStage);
        verify(transitionMapper, times(1)).toDtos(any());

        assertThat(phenotypingStageResponseDTO.getId(), is(1L));
        assertThat(phenotypingStageResponseDTO.getPsn(), is("psn"));
        assertThat(phenotypingStageResponseDTO.getPhenotypingTypeName(), is("typeName"));
        assertThat(phenotypingStageResponseDTO.getStatusName(), is("statusName"));
        assertThat(phenotypingStageResponseDTO.getPhenotypingExternalRef(), is("externalRef"));
    }
}