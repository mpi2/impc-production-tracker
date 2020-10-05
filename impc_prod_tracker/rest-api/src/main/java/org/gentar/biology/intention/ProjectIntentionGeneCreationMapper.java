package org.gentar.biology.intention;

import org.gentar.Mapper;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.gene.mappers.GeneCreationMapper;
import org.gentar.biology.gene.ProjectIntentionGeneCreationDTO;
import org.gentar.biology.intention.project_intention_gene.ProjectIntentionGene;
import org.springframework.stereotype.Component;

@Component
public class ProjectIntentionGeneCreationMapper
    implements Mapper<ProjectIntentionGene, ProjectIntentionGeneCreationDTO>
{
    private final GeneCreationMapper geneCreationMapper;

    public ProjectIntentionGeneCreationMapper(GeneCreationMapper geneCreationMapper)
    {
        this.geneCreationMapper = geneCreationMapper;
    }

    @Override
    public ProjectIntentionGeneCreationDTO toDto(ProjectIntentionGene projectIntentionGene)
    {
        return null;
    }

    @Override
    public ProjectIntentionGene toEntity(ProjectIntentionGeneCreationDTO projectIntentionGeneCreationDTO)
    {
        ProjectIntentionGene projectIntentionGene = new ProjectIntentionGene();
        Gene gene = geneCreationMapper.toEntity(projectIntentionGeneCreationDTO.getGeneDTO());
        projectIntentionGene.setGene(gene);
        return projectIntentionGene;
    }
}
