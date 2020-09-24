package org.gentar.biology.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.gentar.biology.gene.GeneDTO;
import org.gentar.biology.gene.ProjectIntentionGeneDTO;
import org.gentar.biology.intention.ProjectIntentionDTO;
import org.gentar.biology.plan.PlanMinimumCreationDTO;
import org.junit.jupiter.api.Test;
import org.util.JsonConverter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

class ProjectCreationDTOTest
{
    @Test
    public void testStructure() throws JsonProcessingException
    {
        ProjectCreationDTO projectCreationDTO = new ProjectCreationDTO();
        ProjectCommonDataDTO projectCommonDataDTO = new ProjectCommonDataDTO();
        projectCommonDataDTO.setComment("comment");
        projectCommonDataDTO.setPrivacyName("public");
        projectCommonDataDTO.setProjectExternalRef("externalRef");
        LocalDateTime date = LocalDateTime.of(2000, 1, 1, 0, 0);
        projectCommonDataDTO.setReactivationDate(date);
        projectCreationDTO.setProjectCommonDataDTO(projectCommonDataDTO);
        PlanMinimumCreationDTO planMinimumCreationDTO = new PlanMinimumCreationDTO();
        planMinimumCreationDTO.setPlanTypeName("production");
        planMinimumCreationDTO.setAttemptTypeName("crispr");
        projectCreationDTO.setPlanMinimumCreationDTO(planMinimumCreationDTO);
        ProjectIntentionDTO projectIntentionDTO = new ProjectIntentionDTO();
        ProjectIntentionGeneDTO projectIntentionGeneDTO = new ProjectIntentionGeneDTO();
        GeneDTO geneDTO = new GeneDTO();
        geneDTO.setSymbol("symbol");
        projectIntentionGeneDTO.setGeneDTO(geneDTO);
        projectIntentionDTO.setProjectIntentionGeneDTO(projectIntentionGeneDTO);
        List<ProjectIntentionDTO> projectIntentionDTOS = new ArrayList<>();
        projectIntentionDTOS.add(projectIntentionDTO);

        projectCreationDTO.setProjectIntentionDTOS(projectIntentionDTOS);

        String json = JsonConverter.toJson(projectCreationDTO);

        assertThat(json, is(notNullValue()));
        assertThat(json, is("{\"recovery\":null,\"comment\":\"comment\"," +
            "\"reactivationDate\":\"2000-01-01T00:00:00\",\"privacyName\":\"public\"," +
            "\"externalReference\":\"externalRef\",\"planDetails\":{\"attemptTypeName\":\"crispr\"," +
            "\"typeName\":\"production\"},\"projectIntentions\":[{\"molecularMutationTypeName\":null," +
            "\"mutationCategorizations\":null,\"intentionByGene\":{\"gene\":{\"id\":null," +
            "\"name\":null,\"symbol\":\"symbol\",\"speciesName\":null,\"externalLink\":null," +
            "\"accessionId\":null}}}],\"speciesNames\":null,\"consortia\":null}"));
    }
}
