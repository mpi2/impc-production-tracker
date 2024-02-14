package org.gentar.biology.project.consortium;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.project.ProjectConsortiumDTO;
import org.gentar.biology.project.consortium.institute.ProjectConsortiumInstituteMapper;
import org.gentar.organization.consortium.ConsortiumService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class ProjectConsortiumMapper implements Mapper<ProjectConsortium, ProjectConsortiumDTO>
{
    private final EntityMapper entityMapper;
    private final ConsortiumService consortiumService;
    private ProjectConsortiumInstituteMapper projectConsortiumInstituteMapper;

    public ProjectConsortiumMapper(
        ConsortiumService consortiumService,
        EntityMapper entityMapper,
        ProjectConsortiumInstituteMapper projectConsortiumInstituteMapper)
    {
        this.entityMapper = entityMapper;
        this.consortiumService = consortiumService;
        this.projectConsortiumInstituteMapper = projectConsortiumInstituteMapper;
    }

    public ProjectConsortium toEntity(ProjectConsortiumDTO projectConsortiumDTO)
    {
        ProjectConsortium projectConsortium = new ProjectConsortium();
        String consortiumName = projectConsortiumDTO.getConsortiumName();
        projectConsortium.setConsortium(consortiumService.getConsortiumByNameOrThrowException(consortiumName));
//        projectConsortium.setInstitutes(addProjectConsortiumInstituteFromEntity(projectConsortiumDTO));
        return projectConsortium;
    }

    @Override
    public Set<ProjectConsortium> toEntities(Collection<ProjectConsortiumDTO> projectConsortiumDTOS) {
        Set<ProjectConsortium> projectConsortiums = new HashSet<>();
        if (projectConsortiumDTOS != null)
        {
            projectConsortiumDTOS.forEach(projectConsortiumDTO -> projectConsortiums.add(toEntity(projectConsortiumDTO)));
        } else {
            consortiumService.getErrorConsortiumNull();
        }
        return projectConsortiums;
    }

    public ProjectConsortiumDTO toDto(ProjectConsortium projectConsortium)
    {
        //        addProjectConsortiumInstituteFromDto(projectConsortium, projectConsortiumDTO);
        return entityMapper.toTarget(projectConsortium, ProjectConsortiumDTO.class);
    }

}
