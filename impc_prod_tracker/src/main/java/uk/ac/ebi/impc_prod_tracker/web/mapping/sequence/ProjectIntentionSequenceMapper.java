package uk.ac.ebi.impc_prod_tracker.web.mapping.sequence;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_sequence.ProjectIntentionSequence;
import uk.ac.ebi.impc_prod_tracker.web.dto.sequence.ProjectIntentionSequenceDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.project.intention.ProjectIntentionMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class ProjectIntentionSequenceMapper
{
    private SequenceMapper sequenceMapper;
    private ProjectIntentionMapper projectIntentionMapper;

    public ProjectIntentionSequenceMapper(
        SequenceMapper sequenceMapper,
        ProjectIntentionMapper projectIntentionMapper)
    {
        this.sequenceMapper = sequenceMapper;
        this.projectIntentionMapper = projectIntentionMapper;
    }

    public ProjectIntentionSequenceDTO toDto(ProjectIntentionSequence projectIntentionSequence)
    {
        ProjectIntentionSequenceDTO projectIntentionSequenceDTO = new ProjectIntentionSequenceDTO();
        projectIntentionSequenceDTO.setSequenceDTO(
            sequenceMapper.toDto(projectIntentionSequence.getSequence()));
        projectIntentionSequenceDTO.setIndex(projectIntentionSequence.getIndex());
        projectIntentionSequenceDTO.setProjectIntentionDTO(
            projectIntentionMapper.toDto(projectIntentionSequence.getProjectIntention()));
        return projectIntentionSequenceDTO;
    }

    public List<ProjectIntentionSequenceDTO> toDtos(
        Collection<ProjectIntentionSequence> projectIntentionSequences)
    {
        List<ProjectIntentionSequenceDTO> projectIntentionSequenceDTOS = new ArrayList<>();
        if (projectIntentionSequences != null)
        {
            projectIntentionSequences.forEach(x -> projectIntentionSequenceDTOS.add(toDto(x)));
        }
        return projectIntentionSequenceDTOS;

    }
}
