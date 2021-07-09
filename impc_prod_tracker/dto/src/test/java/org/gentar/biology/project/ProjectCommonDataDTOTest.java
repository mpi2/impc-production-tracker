package org.gentar.biology.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.util.JsonConverter;
import java.time.LocalDateTime;

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
        projectCommonDataDTO.setEsCellQcOnly(false);
        LocalDateTime date = LocalDateTime.of(2000, 1, 1, 0, 0);
        projectCommonDataDTO.setReactivationDate(date);

        String json = JsonConverter.toJson(projectCommonDataDTO);

        assertThat(json, is(notNullValue()));
        assertThat(json, is("{\"recovery\":false,\"esCellQcOnly\":false,\"comment\":\"comment\"," +
            "\"reactivationDate\":\"2000-01-01T00:00:00\",\"privacyName\":\"public\"}"));
    }
}
