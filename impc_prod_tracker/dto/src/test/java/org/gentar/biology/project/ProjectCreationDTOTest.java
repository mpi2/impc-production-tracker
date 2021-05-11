package org.gentar.biology.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.gentar.biology.gene.GeneCommonDTO;
import org.gentar.biology.gene.GeneCreationDTO;
import org.gentar.biology.gene.ProjectIntentionGeneCreationDTO;
import org.gentar.biology.intention.ProjectIntentionCreationDTO;
import org.gentar.biology.plan.PlanMinimumCreationDTO;
import org.junit.jupiter.api.Test;
import org.util.JsonConverter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class ProjectCreationDTOTest
{
    @Test
    public void testStructure() throws JsonProcessingException
    {
        ProjectCreationDTO projectCreationDTO = new ProjectCreationDTO();
        ProjectCommonDataDTO projectCommonDataDTO = new ProjectCommonDataDTO();
        projectCommonDataDTO.setComment("comment");
        projectCommonDataDTO.setEsCellQcOnly(true);
        projectCommonDataDTO.setPrivacyName("public");
        LocalDateTime date = LocalDateTime.of(2000, 1, 1, 0, 0);
        projectCommonDataDTO.setReactivationDate(date);
        projectCreationDTO.setProjectCommonDataDTO(projectCommonDataDTO);
        PlanMinimumCreationDTO planMinimumCreationDTO = new PlanMinimumCreationDTO();
        planMinimumCreationDTO.setPlanTypeName("production");
        planMinimumCreationDTO.setAttemptTypeName("crispr");
        projectCreationDTO.setPlanMinimumCreationDTO(planMinimumCreationDTO);
        ProjectIntentionCreationDTO projectIntentionCreationDTO = new ProjectIntentionCreationDTO();
        ProjectIntentionGeneCreationDTO projectIntentionGeneCreationDTO = new ProjectIntentionGeneCreationDTO();
        GeneCreationDTO geneCreationDTO = new GeneCreationDTO();
        GeneCommonDTO geneCommonDTO = new GeneCommonDTO();
        geneCommonDTO.setSymbol("symbol");
        geneCreationDTO.setGeneCommonDTO(geneCommonDTO);
        projectIntentionGeneCreationDTO.setGeneDTO(geneCreationDTO);
        projectIntentionCreationDTO.setProjectIntentionGeneCreationDTO(projectIntentionGeneCreationDTO);
        List<ProjectIntentionCreationDTO> projectIntentionDTOS = new ArrayList<>();
        projectIntentionDTOS.add(projectIntentionCreationDTO);

        projectCreationDTO.setProjectIntentionCreationDTOS(projectIntentionDTOS);

        String json = JsonConverter.toJson(projectCreationDTO);
        assertThat(json, is(notNullValue()));
        assertThat(json, is("{\"recovery\":null,\"esCellQcOnly\":true,\"comment\":\"comment\"," +
            "\"reactivationDate\":\"2000-01-01T00:00:00\",\"privacyName\":\"public\"," +
            "\"planDetails\":{\"attemptTypeName\":\"crispr\"," +
            "\"typeName\":\"production\"},\"projectIntentions\":[{\"intentionByGene\":{\"gene\":" +
            "{\"symbol\":\"symbol\",\"speciesName\":null,\"accessionId\":null}}," +
            "\"intentionsBySequences\":null}],\"speciesNames\":null,\"consortia\":null}"));
    }
}
