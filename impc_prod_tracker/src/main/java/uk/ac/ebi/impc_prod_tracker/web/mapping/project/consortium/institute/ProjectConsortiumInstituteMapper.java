package uk.ac.ebi.impc_prod_tracker.web.mapping.project.consortium.institute;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention.ProjectIntention;
import uk.ac.ebi.impc_prod_tracker.data.organization.institute.Institute;
import uk.ac.ebi.impc_prod_tracker.service.organization.InstituteService;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectConsortiumInstituteDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.EntityMapper;

import java.util.*;

@Component
public class ProjectConsortiumInstituteMapper {
    private EntityMapper entityMapper;
    private InstituteService instituteService;

    public ProjectConsortiumInstituteMapper(EntityMapper entityMapper, InstituteService instituteService)
    {
        this.entityMapper = entityMapper;
        this.instituteService = instituteService;
    }

    public Institute toEntity(ProjectConsortiumInstituteDTO projectConsortiumInstituteDTO)
    {
        Institute institute = entityMapper.toTarget(projectConsortiumInstituteDTO, Institute.class);
        return institute;
    }

    public Set<Institute> toEntities(Collection<ProjectConsortiumInstituteDTO> projectConsortiumInstituteDTOS)
    {
        Set<Institute> institutes = new HashSet<>();
        if (projectConsortiumInstituteDTOS != null)
        {
            projectConsortiumInstituteDTOS.forEach(projectConsortiumInstituteDTO -> institutes.add(toEntity(projectConsortiumInstituteDTO)));
        }
        return institutes;
    }

    public ProjectConsortiumInstituteDTO toDto(Institute institute)
    {
        ProjectConsortiumInstituteDTO projectConsortiumInstituteDTO = entityMapper.toTarget(institute, ProjectConsortiumInstituteDTO.class);

        return projectConsortiumInstituteDTO;
    }

    public List<ProjectConsortiumInstituteDTO> toDtos(Collection<Institute> institutes)
    {
        List<ProjectConsortiumInstituteDTO> instituteDTOS = new ArrayList<>();
        if (institutes != null)
        {
            institutes.forEach(x -> instituteDTOS.add(toDto(x)));
        }
        return instituteDTOS;
    }
}
