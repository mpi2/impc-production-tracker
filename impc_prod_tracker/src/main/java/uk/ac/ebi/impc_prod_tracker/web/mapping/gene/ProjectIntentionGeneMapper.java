package uk.ac.ebi.impc_prod_tracker.web.mapping.gene;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_gene.ProjectIntentionGene;
import uk.ac.ebi.impc_prod_tracker.web.dto.gene.ProjectIntentionGeneDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.project.intention.ProjectIntentionMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class ProjectIntentionGeneMapper
{
    private GeneMapper geneMapper;
    private ProjectIntentionMapper projectIntentionMapper;

    public ProjectIntentionGeneMapper(
        GeneMapper geneMapper, ProjectIntentionMapper projectIntentionMapper) {
        this.geneMapper = geneMapper;
        this.projectIntentionMapper = projectIntentionMapper;
    }

    public ProjectIntentionGeneDTO toDto (ProjectIntentionGene projectIntentionGene)
    {
        ProjectIntentionGeneDTO projectIntentionGeneDTO = new ProjectIntentionGeneDTO();
        projectIntentionGeneDTO.setGeneDTO(geneMapper.toDto(projectIntentionGene.getGene()));
        projectIntentionGeneDTO.setProjectIntentionDTO(
            projectIntentionMapper.toDto(projectIntentionGene.getProjectIntention()));
        return projectIntentionGeneDTO;
    }

    public List<ProjectIntentionGeneDTO> toDtos (Set<ProjectIntentionGene> projectIntentionGenes)
    {
        List<ProjectIntentionGeneDTO> projectIntentionGeneDTOS = new ArrayList<>();
        if (projectIntentionGenes != null)
        {
            projectIntentionGenes.forEach(projectGene -> projectIntentionGeneDTOS.add(toDto(projectGene)));
        }
        return projectIntentionGeneDTOS;
    }
}
