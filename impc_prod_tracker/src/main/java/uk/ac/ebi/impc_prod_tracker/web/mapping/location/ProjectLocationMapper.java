package uk.ac.ebi.impc_prod_tracker.web.mapping.location;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_location.ProjectLocation;
import uk.ac.ebi.impc_prod_tracker.web.dto.location.ProjectLocationDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class ProjectLocationMapper
{
    private LocationMapper locationMapper;

    public ProjectLocationMapper(LocationMapper locationMapper) {
        this.locationMapper = locationMapper;
    }

    public ProjectLocationDTO toDto (ProjectLocation projectLocation)
    {
        ProjectLocationDTO projectLocationDTO = new ProjectLocationDTO();
        projectLocationDTO.setLocationDTO(locationMapper.toDto(projectLocation.getLocation()));
        projectLocationDTO.setIndex(projectLocation.getIndex());
        if (projectLocation.getChromosomeFeatureType() != null)
        {
            projectLocationDTO.setChrFeatureTypeName(projectLocation.getChromosomeFeatureType().getType());
        }
        projectLocationDTO.setAlleleTypeName(projectLocation.getAlleleType().getName());
        projectLocationDTO.setSequence(projectLocation.getSequence());

        return projectLocationDTO;
    }

    public List<ProjectLocationDTO> toDtos (Set<ProjectLocation> projectLocations)
    {
        List<ProjectLocationDTO> projectLocationDTOS = new ArrayList<>();
        projectLocations.forEach(projectLocation -> projectLocationDTOS.add(toDto(projectLocation)));
        return projectLocationDTOS;
    }
}
