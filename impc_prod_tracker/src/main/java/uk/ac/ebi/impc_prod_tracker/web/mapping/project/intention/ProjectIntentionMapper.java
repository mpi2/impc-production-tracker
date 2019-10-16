package uk.ac.ebi.impc_prod_tracker.web.mapping.project.intention;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention.ProjectIntention;
import uk.ac.ebi.impc_prod_tracker.web.dto.ProjectIntentionDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.EntityMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class ProjectIntentionMapper
{
    private EntityMapper entityMapper;

    public ProjectIntentionMapper(EntityMapper entityMapper)
    {
        this.entityMapper = entityMapper;
    }

    public ProjectIntentionDTO toDto (ProjectIntention projectIntention)
    {
        return entityMapper.toTarget(projectIntention, ProjectIntentionDTO.class);
    }

    public List<ProjectIntentionDTO> toDtos (Set<ProjectIntention> projectIntention)
    {
        List<ProjectIntentionDTO> intentionDTOS = new ArrayList<>();
        if (projectIntention != null)
        {
            projectIntention.forEach(x -> intentionDTOS.add(toDto(x)));
        }
        return intentionDTOS;
    }
}
