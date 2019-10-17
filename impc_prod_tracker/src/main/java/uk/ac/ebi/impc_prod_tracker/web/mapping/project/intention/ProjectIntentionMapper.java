package uk.ac.ebi.impc_prod_tracker.web.mapping.project.intention;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention.ProjectIntention;
import uk.ac.ebi.impc_prod_tracker.web.dto.intention.ProjectIntentionDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.EntityMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.allele_categorization.AlleleCategorizationMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class ProjectIntentionMapper
{
    private EntityMapper entityMapper;
    private AlleleCategorizationMapper alleleCategorizationMapper;

    public ProjectIntentionMapper(
        EntityMapper entityMapper, AlleleCategorizationMapper alleleCategorizationMapper)
    {
        this.entityMapper = entityMapper;
        this.alleleCategorizationMapper = alleleCategorizationMapper;
    }

    public ProjectIntentionDTO toDto (ProjectIntention projectIntention)
    {
        ProjectIntentionDTO projectIntentionDTO =
            entityMapper.toTarget(projectIntention, ProjectIntentionDTO.class);
        projectIntentionDTO.setAlleleCategorizationDTOS(
            alleleCategorizationMapper.toDtos(projectIntention.getAlleleCategorizations()));
        return projectIntentionDTO;
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
