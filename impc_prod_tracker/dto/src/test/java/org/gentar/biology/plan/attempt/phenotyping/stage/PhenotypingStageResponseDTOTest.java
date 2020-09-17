package org.gentar.biology.plan.attempt.phenotyping.stage;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.gentar.common.state_machine.TransitionDTO;
import org.junit.jupiter.api.Test;
import org.util.JsonConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

class PhenotypingStageResponseDTOTest
{
    @Test
    public void testStructure() throws JsonProcessingException
    {
        PhenotypingStageResponseDTO phenotypingStageResponseDTO = new PhenotypingStageResponseDTO();
        phenotypingStageResponseDTO.setPin("pin");
        phenotypingStageResponseDTO.setPsn("psn");
        phenotypingStageResponseDTO.setStatusName("statusName");
        phenotypingStageResponseDTO.setPhenotypingTypeName("phenotypingTypeName");
        phenotypingStageResponseDTO.setPhenotypingExternalRef("phenotypingExternalRef");
        PhenotypingStageCommonDTO phenotypingStageCommonDTO = new PhenotypingStageCommonDTO();
        phenotypingStageCommonDTO.setPhenotypingExperimentsStarted(LocalDate.parse("2020-06-17"));
        phenotypingStageCommonDTO.setInitialDataReleaseDate(LocalDate.parse("2020-06-17"));
        phenotypingStageResponseDTO.setPhenotypingStageCommonDTO(phenotypingStageCommonDTO);
        StatusTransitionDTO statusTransitionDTO = new StatusTransitionDTO();
        statusTransitionDTO.setActionToExecute("actionToExecute");
        phenotypingStageResponseDTO.setStatusTransitionDTO(statusTransitionDTO);

        phenotypingStageResponseDTO.setStatusStampsDTOS(buildStatusStampsDTOS());
        phenotypingStageResponseDTO.setStatusTransitionDTO(buildStatusTransitionDTO());

        String json = JsonConverter.toJson(phenotypingStageResponseDTO);
        assertThat(json, is(notNullValue()));
        assertThat(json, is("{\"pin\":\"pin\",\"psn\":\"psn\",\"statusName\":\"statusName\"," +
            "\"phenotypingTypeName\":\"phenotypingTypeName\"," +
            "\"phenotypingExternalRef\":\"phenotypingExternalRef\"," +
            "\"phenotypingExperimentsStarted\":\"2020-06-17\"," +
            "\"initialDataReleaseDate\":\"2020-06-17\"," +
            "\"tissueDistributions\":null," +
            "\"links\":[],\"statusDates\":[{\"statusName\":null,\"date\":\"2020-01-01T01:01:00\"}]," +
            "\"statusTransition\":{\"currentStatus\":\"current status name\"," +
            "\"transitions\":[{\"action\":null,\"description\":\"transition description\"," +
            "\"triggeredByUser\":false,\"available\":false,\"note\":null,\"nextStatus\":null}]," +
            "\"actionToExecute\":null}}"));
    }

    private List<StatusStampsDTO> buildStatusStampsDTOS()
    {
        List<StatusStampsDTO> statusStampsDTOS = new ArrayList<>();
        StatusStampsDTO statusStampsDTO = new StatusStampsDTO();
        statusStampsDTO.setDate(LocalDateTime.of(2020, 1, 1, 1, 1));
        statusStampsDTOS.add(statusStampsDTO);
        return statusStampsDTOS;
    }

    private StatusTransitionDTO buildStatusTransitionDTO()
    {
        StatusTransitionDTO statusTransitionDTO = new StatusTransitionDTO();
        statusTransitionDTO.setCurrentStatus("current status name");
        List<TransitionDTO> transitionDTOS = new ArrayList<>();
        TransitionDTO transitionDTO = new TransitionDTO();
        transitionDTO.setDescription("transition description");
        transitionDTOS.add(transitionDTO);
        statusTransitionDTO.setTransitions(transitionDTOS);
        return statusTransitionDTO;
    }
}