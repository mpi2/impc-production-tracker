package org.gentar.biology.plan;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.util.JsonConverter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

class PlanMinimumCreationDTOTest
{
    @Test
    public void testStructure() throws JsonProcessingException
    {
        PlanMinimumCreationDTO planMinimumCreationDTO = new PlanMinimumCreationDTO();
        planMinimumCreationDTO.setAttemptTypeName("attemptTypeName");
        planMinimumCreationDTO.setPlanTypeName("planTypeName");
        PlanCommonDataDTO planCommonDataDTO = new PlanCommonDataDTO();
        planCommonDataDTO.setComment("comment");
        planMinimumCreationDTO.setPlanCommonDataDTO(planCommonDataDTO);

        String json = JsonConverter.toJson(planMinimumCreationDTO);
        assertThat(json, is(notNullValue()));
        assertThat(json, is("{\"funderNames\":null,\"workUnitName\":null,\"workGroupName\":null," +
            "\"comment\":\"comment\",\"productsAvailableForGeneralPublic\":null," +
            "\"attemptTypeName\":\"attemptTypeName\",\"typeName\":\"planTypeName\"}"));
    }
}