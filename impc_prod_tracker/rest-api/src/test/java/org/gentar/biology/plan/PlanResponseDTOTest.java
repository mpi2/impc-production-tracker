package org.gentar.biology.plan;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.gentar.biology.plan.attempt.crispr.CrisprAttemptDTO;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.gentar.common.state_machine.TransitionDTO;
import org.gentar.util.JsonHelper;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

class PlanResponseDTOTest
{
    @Test
    public void test() throws JsonProcessingException
    {
        PlanResponseDTO planResponseDTO = new PlanResponseDTO();
        PlanBasicDataDTO planBasicDataDTO = new PlanBasicDataDTO();
        CrisprAttemptDTO crisprAttemptDTO = new CrisprAttemptDTO();
        crisprAttemptDTO.setExperimental(true);
        crisprAttemptDTO.setComment("crispr comment");
        planBasicDataDTO.setCrisprAttemptDTO(crisprAttemptDTO);
        PlanCreationDTO planCreationDTO = new PlanCreationDTO();
        planCreationDTO.setPlanBasicDataDTO(planBasicDataDTO);
        planResponseDTO.setPlanCreationDTO(planCreationDTO);
        planResponseDTO.setTpn("tpn");
        planResponseDTO.setPin("pin");
        planResponseDTO.setStatusName("status name");
        planResponseDTO.setSummaryStatusName("summary status name");
        planResponseDTO.setStatusStampsDTOS(buildStatusStampsDTOS());
        planResponseDTO.setStatusTransitionDTO(buildStatusTransitionDTO());
        String json = JsonHelper.toJson(planResponseDTO);
        System.out.println(json);
        assertThat(json, is(notNullValue()));
        assertThat(json, is("{\"pin\":\"pin\",\"tpn\":\"tpn\",\"statusName\":\"status name\"," +
            "\"summaryStatusName\":\"summary status name\",\"planTypeName\":null," +
            "\"attemptTypeName\":null,\"crisprAttempt\":{\"miDate\":null,\"experimental\":true," +
            "\"comment\":\"crispr comment\",\"mutagenesisExternalRef\":null," +
            "\"attemptExternalRef\":null,\"nucleases\":null,\"guides\":null," +
            "\"mutagenesisDonors\":null,\"reagents\":null,\"genotypePrimers\":null," +
            "\"totalEmbryosInjected\":null,\"totalEmbryosSurvived\":null," +
            "\"embryo2Cell\":null,\"assay\":null,\"strainInjectedName\":null},\"links\":[]," +
            "\"statusDates\":[{\"statusName\":null,\"date\":\"2020-01-01T01:01:00\"}]," +
            "\"summaryStatusDates\":null," +
            "\"statusTransition\":{\"currentStatus\":\"current status name\"," +
            "\"transitions\":[{\"action\":null,\"description\":\"transition description\"," +
            "\"triggeredByUser\":false,\"available\":false,\"note\":null," +
            "\"nextStatus\":null}],\"actionToExecute\":null}}"));
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