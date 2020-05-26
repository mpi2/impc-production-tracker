package org.gentar.biology.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.gentar.util.JsonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        projectCommonDataDTO.setSpeciesNames(Arrays.asList("species1"));
        ProjectConsortiumDTO projectConsortiumDTO = new ProjectConsortiumDTO();
        projectConsortiumDTO.setConsortiumName("consortiumName");
        projectConsortiumDTO.setInstituteNames(Arrays.asList("institute1"));
        List<ProjectConsortiumDTO> projectConsortiumDTOS = new ArrayList<>();
        projectConsortiumDTOS.add(projectConsortiumDTO);
        projectCommonDataDTO.setProjectConsortiumDTOS(projectConsortiumDTOS);

        String json = JsonHelper.toJson(projectCommonDataDTO);

        assertThat(json, is(notNullValue()));
        assertThat(json, is("{\"recovery\":false," +
            "\"comment\":\"comment\",\"privacyName\":\"public\"," +
            "\"externalReference\":\"externalRef\",\"speciesNames\":[\"species1\"]," +
            "\"consortia\":[{\"consortiumName\":\"consortiumName\"," +
            "\"institutes\":[\"institute1\"]}]}"));
    }
}