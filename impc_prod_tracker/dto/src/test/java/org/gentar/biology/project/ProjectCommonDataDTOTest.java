package org.gentar.biology.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.util.JsonConverter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

class ProjectCommonDataDTOTest
{
    @Test
    public void testStructure() throws JsonProcessingException
    {
        ProjectCommonDataDTO projectCommonDataDTO = new ProjectCommonDataDTO();
        projectCommonDataDTO.setComment("comment");
        projectCommonDataDTO.setPrivacyName("public");
        projectCommonDataDTO.setRecovery(false);
        projectCommonDataDTO.setProjectExternalRef("externalRef");

        String json = JsonConverter.toJson(projectCommonDataDTO);
        System.out.println(json);

        assertThat(json, is(notNullValue()));
        assertThat(json, is("{\"recovery\":false,\"comment\":\"comment\"," +
            "\"privacyName\":\"public\",\"externalReference\":\"externalRef\"}"));
    }
}
