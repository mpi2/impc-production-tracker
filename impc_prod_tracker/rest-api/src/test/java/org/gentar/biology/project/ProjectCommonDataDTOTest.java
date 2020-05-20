package org.gentar.biology.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.gentar.biology.plan.PlanBasicDataDTO;
import org.gentar.biology.plan.PlanCommonDataDTO;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttemptDTO;
import org.gentar.biology.plan.plan_starting_point.PlanStartingPointDTO;
import org.gentar.util.JsonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

class ProjectCommonDataDTOTest
{

    @BeforeEach
    void setUp()
    {
    }

    @Test
    public void testStructure() throws JsonProcessingException
    {
        ProjectCommonDataDTO projectCommonDataDTO = new ProjectCommonDataDTO();
        projectCommonDataDTO.setComment("comment");
        projectCommonDataDTO.setPrivacyName("public");
        projectCommonDataDTO.setRecovery(false);
        projectCommonDataDTO.setReactivationDate(LocalDateTime.of(2020,1,1,1,1));
        projectCommonDataDTO.setProjectExternalRef("externalRef");
        projectCommonDataDTO.setSpeciesNames(Arrays.asList("species1"));
        ProjectConsortiumDTO projectConsortiumDTO = new ProjectConsortiumDTO();
        projectConsortiumDTO.setConsortiumName("consortiumName");
        projectConsortiumDTO.setProjectConsortiumInstituteNames(Arrays.asList("institute1"));
        List<ProjectConsortiumDTO> projectConsortiumDTOS = new ArrayList<>();
        projectConsortiumDTOS.add(projectConsortiumDTO);
        projectCommonDataDTO.setProjectConsortiumDTOS(projectConsortiumDTOS);

        String json = JsonHelper.toJson(projectCommonDataDTO);
        System.out.println(json);

        assertThat(json, is(notNullValue()));
//        assertThat(json, is("{\"funderNames\":null,\"workUnitName\":null,\"workGroupName\":null," +
//            "\"comment\":null,\"productsAvailableForGeneralPublic\":null," +
//            "\"phenotypingStartingPoint\":{\"outcomeTpo\":null}," +
//            "\"phenotypingAttempt\":{\"phenotypingExternalRef\":null," +
//            "\"phenotypingBackgroundStrainName\":null,\"phenotypingStages\":null}}"));
    }
}