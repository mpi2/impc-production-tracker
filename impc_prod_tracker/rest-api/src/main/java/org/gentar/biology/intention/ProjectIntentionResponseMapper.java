package org.gentar.biology.intention;

import org.gentar.Mapper;
import org.gentar.biology.intention.project_intention.ProjectIntention;
import org.springframework.stereotype.Component;

@Component
public class ProjectIntentionResponseMapper implements Mapper<ProjectIntention, ProjectIntentionResponseDTO>
{
    private final ProjectIntentionCommonMapper projectIntentionCommonMapper;
    private final ProjectIntentionGeneResponseMapper projectIntentionGeneResponseMapper;
    private final ProjectIntentionSequenceMapper projectIntentionSequenceMapper;

    public ProjectIntentionResponseMapper(
        ProjectIntentionCommonMapper projectIntentionCommonMapper,
        ProjectIntentionGeneResponseMapper projectIntentionGeneResponseMapper,
        ProjectIntentionSequenceMapper projectIntentionSequenceMapper)
    {
        this.projectIntentionCommonMapper = projectIntentionCommonMapper;
        this.projectIntentionGeneResponseMapper = projectIntentionGeneResponseMapper;
        this.projectIntentionSequenceMapper = projectIntentionSequenceMapper;
    }

    @Override
    public ProjectIntentionResponseDTO toDto(ProjectIntention projectIntention)
    {
        ProjectIntentionResponseDTO projectIntentionResponseDTO = new ProjectIntentionResponseDTO();
        projectIntentionResponseDTO.setProjectIntentionCommonDTO(
            projectIntentionCommonMapper.toDto(projectIntention));
        projectIntentionResponseDTO.setProjectIntentionGeneDTO(
            projectIntentionGeneResponseMapper.toDto(projectIntention.getProjectIntentionGene()));
        // TODO: Sequences
        projectIntentionResponseDTO.setProjectIntentionSequenceDTOS(
            projectIntentionSequenceMapper.toDtos(projectIntention.getProjectIntentionSequences()));
        return projectIntentionResponseDTO;
    }

    @Override
    public ProjectIntention toEntity(ProjectIntentionResponseDTO dto)
    {
        return null;
    }
}
