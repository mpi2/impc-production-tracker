package uk.ac.ebi.impc_prod_tracker.web.mapping.project.consortium;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_consortium.ProjectConsortium;
import uk.ac.ebi.impc_prod_tracker.service.organization.consortium.ConsortiumService;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectConsortiumDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.EntityMapper;

import java.util.*;

@Component
public class ProjectConsortiumMapper {
    private EntityMapper entityMapper;
    private ConsortiumService consortiumService;

    public ProjectConsortiumMapper(ConsortiumService consortiumService, EntityMapper entityMapper)
    {
        this.entityMapper = entityMapper;
        this.consortiumService = consortiumService;
    }

    public ProjectConsortium toEntity(ProjectConsortiumDTO projectConsortiumDTO)
    {
        ProjectConsortium projectConsortium = new ProjectConsortium();
        projectConsortium.setId(getId(projectConsortiumDTO));
        String consortiumName = projectConsortiumDTO.getConsortiumName();
        projectConsortium.setConsortium(consortiumService.getConsortiumByNameOrThrowException(consortiumName));
        return projectConsortium;
    }

    private Long getId(ProjectConsortiumDTO projectConsortiumDTO)
    {
        Long id = null;
        Long dtoId = projectConsortiumDTO.getId();
        if (dtoId != null)
        {
            id = dtoId  > 0 ? null : dtoId;
        }
        return id;
    }

    public Set<ProjectConsortium> toEntities(Collection<ProjectConsortiumDTO> projectConsortiumDTOS)
    {
        Set<ProjectConsortium> projectConsortia = new HashSet<>();
        if (projectConsortiumDTOS != null)
        {
            projectConsortiumDTOS.forEach(x -> projectConsortia.add(toEntity(x)));
        }
        return projectConsortia;
    }

    public ProjectConsortiumDTO toDto(ProjectConsortium projectConsortium)
    {
        ProjectConsortiumDTO projectConsortiumnDTO = entityMapper.toTarget(projectConsortium, ProjectConsortiumDTO.class);

        return projectConsortiumnDTO;
    }

    public List<ProjectConsortiumDTO> toDtos(Collection<ProjectConsortium> projectConsortium)
    {
        List<ProjectConsortiumDTO> consortiumDTOS = new ArrayList<>();
        if (projectConsortium != null)
        {
            projectConsortium.forEach(x -> consortiumDTOS.add(toDto(x)));
        }
        return consortiumDTOS;
    }
}
