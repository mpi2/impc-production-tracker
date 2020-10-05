package org.gentar.biology.intention;

import org.gentar.Mapper;
import org.gentar.biology.gene.ProjectIntentionGeneResponseDTO;
import org.gentar.biology.gene.mappers.GeneResponseMapper;
import org.gentar.biology.intention.project_intention_gene.ProjectIntentionGene;
import org.springframework.stereotype.Component;

@Component
public class ProjectIntentionGeneResponseMapper
    implements Mapper<ProjectIntentionGene, ProjectIntentionGeneResponseDTO>
{
    private final GeneResponseMapper geneResponseMapper;
    private final OrthologMapper orthologMapper;

    public ProjectIntentionGeneResponseMapper(
        GeneResponseMapper geneResponseMapper, OrthologMapper orthologMapper)
    {
        this.geneResponseMapper = geneResponseMapper;
        this.orthologMapper = orthologMapper;
    }

    @Override
    public ProjectIntentionGeneResponseDTO toDto(ProjectIntentionGene projectIntentionGene)
    {
        ProjectIntentionGeneResponseDTO projectIntentionGeneResponseDTO
            = new ProjectIntentionGeneResponseDTO();

        projectIntentionGeneResponseDTO.setGeneDTO(
            geneResponseMapper.toDto(projectIntentionGene.getGene()));

        projectIntentionGeneResponseDTO.setAllOrthologs(
            orthologMapper.toDtos(projectIntentionGene.getAllOrthologs()));

        projectIntentionGeneResponseDTO.setBestOrthologs(
            orthologMapper.toDtos(projectIntentionGene.getBestOrthologs()));

        return projectIntentionGeneResponseDTO;
    }

    @Override
    public ProjectIntentionGene toEntity(ProjectIntentionGeneResponseDTO projectIntentionGeneResponseDTO)
    {
        return null;
    }
}
