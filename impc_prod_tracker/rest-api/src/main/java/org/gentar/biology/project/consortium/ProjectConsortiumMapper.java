package org.gentar.biology.project.consortium;

import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.crispr.CrisprAttemptReagentDTO;
import org.gentar.biology.plan.attempt.crispr.reagent.CrisprAttemptReagent;
import org.gentar.organization.consortium.ConsortiumService;
import org.gentar.biology.project.ProjectConsortiumDTO;
import org.gentar.EntityMapper;
import org.gentar.organization.institute.Institute;
import org.springframework.stereotype.Component;
import org.gentar.biology.project.consortium.institute.ProjectConsortiumInstituteMapper;
import java.util.*;

@Component
public class ProjectConsortiumMapper implements Mapper<ProjectConsortium, ProjectConsortiumDTO>
{
    private EntityMapper entityMapper;
    private ConsortiumService consortiumService;
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
        projectConsortium.setConsortium(
            consortiumService.getConsortiumByNameOrThrowException(consortiumName));
        projectConsortium.setInstitutes(
            addProjectConsortiumInstituteFromEntity(projectConsortiumDTO));

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
        ProjectConsortiumDTO projectConsortiumDTO =
            entityMapper.toTarget(projectConsortium, ProjectConsortiumDTO.class);
        addProjectConsortiumInstituteFromDto(projectConsortium, projectConsortiumDTO);
        return projectConsortiumDTO;
    }

    private void addProjectConsortiumInstituteFromDto(
        ProjectConsortium projectConsortium, ProjectConsortiumDTO projectConsortiumDTO)
    {
        List<String> projectConsortiumInstituteNames =
                projectConsortiumInstituteMapper.toDtos(projectConsortium.getInstitutes());
        projectConsortiumDTO.setInstituteNames(projectConsortiumInstituteNames);
    }

    private Set<Institute> addProjectConsortiumInstituteFromEntity(
        ProjectConsortiumDTO projectConsortiumDTO)
    {
        return new HashSet<>(projectConsortiumInstituteMapper.toEntities(
            projectConsortiumDTO.getInstituteNames()));
    }
}
