package org.gentar.biology.plan.attempt.phenotyping.stage;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.junit.jupiter.api.Test;
import org.util.JsonConverter;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

class PhenotypingStageUpdateDTOTest
{
    @Test
    public void testStructure() throws JsonProcessingException
    {
        PhenotypingStageUpdateDTO phenotypingStageUpdateDTO = new PhenotypingStageUpdateDTO();
        phenotypingStageUpdateDTO.setPsn("psn");
        PhenotypingStageCommonDTO phenotypingStageCommonDTO = new PhenotypingStageCommonDTO();
        phenotypingStageCommonDTO.setPhenotypingExperimentsStarted(LocalDate.parse("2020-06-17"));
        phenotypingStageCommonDTO.setDoNotCountTowardsCompleteness(true);
        phenotypingStageCommonDTO.setInitialDataReleaseDate(LocalDate.parse("2020-06-17"));
        phenotypingStageUpdateDTO.setPhenotypingStageCommonDTO(phenotypingStageCommonDTO);
        StatusTransitionDTO statusTransitionDTO = new StatusTransitionDTO();
        statusTransitionDTO.setActionToExecute("actionToExecute");
        phenotypingStageUpdateDTO.setStatusTransitionDTO(statusTransitionDTO);

        String json = JsonConverter.toJson(phenotypingStageUpdateDTO);
        assertThat(json, is(notNullValue()));
        assertThat(json, is("{\"id\":null,\"psn\":\"psn\",\"phenotypingExperimentsStarted\":\"2020-06-17\"," +
                "\"doNotCountTowardsCompleteness\":true,\"initialDataReleaseDate\":\"2020-06-17\"," +
                "\"tissueDistributions\":null,\"statusTransition\":{\"actionToExecute\":\"actionToExecute\"}}"));
    }

}