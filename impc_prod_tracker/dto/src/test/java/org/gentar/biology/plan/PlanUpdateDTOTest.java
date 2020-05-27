package org.gentar.biology.plan;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.gentar.biology.plan.attempt.crispr.CrisprAttemptDTO;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.junit.jupiter.api.Test;
import org.util.JsonConverter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

class PlanUpdateDTOTest
{
    @Test
    public void test() throws JsonProcessingException
    {
        PlanUpdateDTO planUpdateDTO = new PlanUpdateDTO();
        PlanBasicDataDTO planBasicDataDTO = new PlanBasicDataDTO();
        CrisprAttemptDTO crisprAttemptDTO = new CrisprAttemptDTO();
        crisprAttemptDTO.setExperimental(true);
        crisprAttemptDTO.setComment("comment");
        planBasicDataDTO.setCrisprAttemptDTO(crisprAttemptDTO);
        planUpdateDTO.setPlanBasicDataDTO(planBasicDataDTO);
        PlanCommonDataDTO planCommonDataDTO = new PlanCommonDataDTO();
        planCommonDataDTO.setComment("plan comment");
        planBasicDataDTO.setPlanCommonDataDTO(planCommonDataDTO);
        StatusTransitionDTO statusTransitionDTO = new StatusTransitionDTO();
        statusTransitionDTO.setActionToExecute("actionToExecute");
        planUpdateDTO.setStatusTransitionDTO(statusTransitionDTO);
        String json = JsonConverter.toJson(planUpdateDTO);
        assertThat(json, is(notNullValue()));
        assertThat(json, is("{\"pin\":null,\"tpn\":null,\"funderNames\":null,\"workUnitName\":null," +
            "\"workGroupName\":null,\"comment\":\"plan comment\"," +
            "\"productsAvailableForGeneralPublic\":null,\"crisprAttempt\":{\"miDate\":null," +
            "\"experimental\":true,\"comment\":\"comment\",\"mutagenesisExternalRef\":null," +
            "\"attemptExternalRef\":null,\"nucleases\":null,\"guides\":null," +
            "\"mutagenesisDonors\":null,\"reagents\":null,\"genotypePrimers\":null," +
            "\"totalEmbryosInjected\":null,\"totalEmbryosSurvived\":null,\"embryo2Cell\":null," +
            "\"assay\":null,\"strainInjectedName\":null}," +
            "\"statusTransition\":{\"actionToExecute\":\"actionToExecute\"}}"));
    }
}