package uk.ac.ebi.impc_prod_tracker.web.mapping.gene;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_gene.ProjectGene;
import uk.ac.ebi.impc_prod_tracker.web.dto.gene.ProjectGeneDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class ProjectGeneMapper
{
    private GeneMapper geneMapper;

    public ProjectGeneMapper(GeneMapper geneMapper) {
        this.geneMapper = geneMapper;
    }

    public ProjectGeneDTO toDto (ProjectGene projectGene)
    {
        ProjectGeneDTO projectGeneDTO = new ProjectGeneDTO();
        projectGeneDTO.setGeneDTO(geneMapper.toDto(projectGene.getGene()));
        projectGeneDTO.setAlleleTypeName(projectGene.getAlleleType().getName());
        return projectGeneDTO;
    }

    public List<ProjectGeneDTO> toDtos (Set<ProjectGene> projectGenes)
    {
        List<ProjectGeneDTO> projectGeneDTOS = new ArrayList<>();
        projectGenes.forEach(projectGene -> projectGeneDTOS.add(toDto(projectGene)));
        return projectGeneDTOS;
    }
}
