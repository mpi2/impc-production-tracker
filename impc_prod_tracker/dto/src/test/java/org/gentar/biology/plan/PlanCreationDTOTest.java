package org.gentar.biology.plan;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.util.JsonConverter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

class PlanCreationDTOTest
{
    @Test
    public void testStructure() throws JsonProcessingException
    {
        PlanCreationDTO planCreationDTO = new PlanCreationDTO();
        planCreationDTO.setPlanBasicDataDTO(new PlanBasicDataDTO());
        planCreationDTO.setTpn("tpn");
        planCreationDTO.setPlanTypeName("planTypeName");
        planCreationDTO.setAttemptTypeName("attemptTypeName");

        String json = JsonConverter.toJson(planCreationDTO);
        assertThat(json, is(notNullValue()));
        assertThat(json, is("{\"tpn\":\"tpn\",\"attemptTypeName\":\"attemptTypeName\"," +
            "\"typeName\":\"planTypeName\"}"));
    }
}
