package uk.ac.ebi.impc_prod_tracker.web.mapping.location;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_location.ProjectIntentionLocation;
import uk.ac.ebi.impc_prod_tracker.web.dto.location.ProjectIntentionLocationDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.project.intention.ProjectIntentionMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class ProjectIntentionLocationMapper
{
    private LocationMapper locationMapper;
    private ProjectIntentionMapper projectIntentionMapper;

    public ProjectIntentionLocationMapper(
        LocationMapper locationMapper, ProjectIntentionMapper projectIntentionMapper)
    {
        this.locationMapper = locationMapper;
        this.projectIntentionMapper = projectIntentionMapper;
    }

    public ProjectIntentionLocationDTO toDto (ProjectIntentionLocation projectIntentionLocation)
    {
        ProjectIntentionLocationDTO projectIntentionLocationDTO = new ProjectIntentionLocationDTO();
        projectIntentionLocationDTO.setLocationDTO(locationMapper.toDto(projectIntentionLocation.getLocation()));
        if (projectIntentionLocation.getChromosomeFeatureType() != null)
        {
            projectIntentionLocationDTO.setChrFeatureTypeName(projectIntentionLocation.getChromosomeFeatureType().getName());
        }
        projectIntentionLocationDTO.setIndex(projectIntentionLocation.getIndex());
        projectIntentionLocationDTO.setProjectIntentionDTO(
            projectIntentionMapper.toDto(projectIntentionLocation.getProjectIntention()));

        return projectIntentionLocationDTO;
    }

    public List<ProjectIntentionLocationDTO> toDtos (Collection<ProjectIntentionLocation> projectIntentionLocations)
    {
        List<ProjectIntentionLocationDTO> projectIntentionLocationDTOS = new ArrayList<>();
        projectIntentionLocations.forEach(projectLocation -> projectIntentionLocationDTOS.add(toDto(projectLocation)));
        return projectIntentionLocationDTOS;
    }
}
