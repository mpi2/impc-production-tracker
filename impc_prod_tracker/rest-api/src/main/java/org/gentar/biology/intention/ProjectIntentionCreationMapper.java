package org.gentar.biology.intention;

import org.gentar.Mapper;
import org.gentar.biology.gene.ProjectIntentionGeneCreationDTO;
import org.gentar.biology.intention.project_intention.ProjectIntention;
import org.gentar.biology.intention.project_intention_gene.ProjectIntentionGene;
import org.gentar.biology.intention.project_intention_sequence.ProjectIntentionSequence;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class ProjectIntentionCreationMapper implements Mapper<ProjectIntention, ProjectIntentionCreationDTO>
{
    private final ProjectIntentionCommonMapper projectIntentionCommonMapper;
    private final ProjectIntentionGeneCreationMapper projectIntentionGeneCreationMapper;
    private final ProjectIntentionSequenceMapper projectIntentionSequenceMapper;

    public ProjectIntentionCreationMapper(
        ProjectIntentionCommonMapper projectIntentionCommonMapper,
        ProjectIntentionGeneCreationMapper projectIntentionGeneCreationMapper,
        ProjectIntentionSequenceMapper projectIntentionSequenceMapper)
    {
        this.projectIntentionCommonMapper = projectIntentionCommonMapper;
        this.projectIntentionGeneCreationMapper = projectIntentionGeneCreationMapper;
        this.projectIntentionSequenceMapper = projectIntentionSequenceMapper;
    }

    @Override
    public ProjectIntentionCreationDTO toDto(ProjectIntention projectIntention)
    {
        return null;
    }

    @Override
    public ProjectIntention toEntity(ProjectIntentionCreationDTO projectIntentionCreationDTO)
    {
        ProjectIntention projectIntention = new ProjectIntention();
        if (projectIntentionCreationDTO.getProjectIntentionCommonDTO() != null)
        {
            projectIntention = projectIntentionCommonMapper.toEntity(
                projectIntentionCreationDTO.getProjectIntentionCommonDTO());
        }
        setProjectIntentionGene(projectIntention, projectIntentionCreationDTO);
        setProjectIntentionsSequences(projectIntention, projectIntentionCreationDTO);
        return projectIntention;
    }

    private void setProjectIntentionGene(
        ProjectIntention projectIntention, ProjectIntentionCreationDTO projectIntentionCreationDTO)
    {
        ProjectIntentionGeneCreationDTO projectIntentionGeneCreationDTO =
            projectIntentionCreationDTO.getProjectIntentionGeneCreationDTO();
        if (projectIntentionGeneCreationDTO != null)
        {
            ProjectIntentionGene projectIntentionGene =
                projectIntentionGeneCreationMapper.toEntity(projectIntentionGeneCreationDTO);
            projectIntention.setProjectIntentionGene(projectIntentionGene);
            projectIntentionGene.setProjectIntention(projectIntention);
        } else {
            throw new UserOperationFailedException("Gene symbol cannot be null.");
        }
    }

    private void setProjectIntentionsSequences(
        ProjectIntention projectIntention, ProjectIntentionCreationDTO projectIntentionCreationDTO)
    {
        Set<ProjectIntentionSequence> projectIntentionSequences =
            projectIntentionSequenceMapper.toEntities(
                projectIntentionCreationDTO.getProjectIntentionSequenceDTO());
        projectIntention.setProjectIntentionSequences(projectIntentionSequences);
        projectIntentionSequences.forEach(x -> x.setProjectIntention(projectIntention));
    }
}
