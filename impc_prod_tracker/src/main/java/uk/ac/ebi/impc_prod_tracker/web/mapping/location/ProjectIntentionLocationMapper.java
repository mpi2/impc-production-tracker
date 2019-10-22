package uk.ac.ebi.impc_prod_tracker.web.mapping.location;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_location.ProjectIntentionLocation;
import uk.ac.ebi.impc_prod_tracker.web.dto.location.ProjectIntentionLocationDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class ProjectIntentionLocationMapper
{
    private LocationMapper locationMapper;

    public ProjectIntentionLocationMapper(LocationMapper locationMapper)
    {
        this.locationMapper = locationMapper;
    }

    public ProjectIntentionLocationDTO toDto(ProjectIntentionLocation projectIntentionLocation)
    {
        ProjectIntentionLocationDTO projectIntentionLocationDTO = new ProjectIntentionLocationDTO();
        projectIntentionLocationDTO.setLocationDTO(locationMapper.toDto(projectIntentionLocation.getLocation()));
        if (projectIntentionLocation.getChromosomeFeatureType() != null)
        {
            projectIntentionLocationDTO.setChrFeatureTypeName(projectIntentionLocation.getChromosomeFeatureType().getName());
        }
        projectIntentionLocationDTO.setIndex(projectIntentionLocation.getIndex());

        return projectIntentionLocationDTO;
    }

    public List<ProjectIntentionLocationDTO> toDtos (Collection<ProjectIntentionLocation> projectIntentionLocations)
    {
        List<ProjectIntentionLocationDTO> projectIntentionLocationDTOS = new ArrayList<>();
        projectIntentionLocations.forEach(projectLocation -> projectIntentionLocationDTOS.add(toDto(projectLocation)));
        return projectIntentionLocationDTOS;
    }

    public ProjectIntentionLocation toEntity(ProjectIntentionLocationDTO projectIntentionLocationDTO)
    {
        ProjectIntentionLocation projectIntentionLocation = new ProjectIntentionLocation();
        projectIntentionLocation.setLocation(locationMapper.toEntity(projectIntentionLocationDTO.getLocationDTO()));
        return projectIntentionLocation;
    }
}
