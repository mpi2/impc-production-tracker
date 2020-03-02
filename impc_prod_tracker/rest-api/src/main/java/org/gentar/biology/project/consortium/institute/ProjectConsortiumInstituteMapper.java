package org.gentar.biology.project.consortium.institute;

import org.gentar.organization.institute.InstituteService;
import org.gentar.biology.project.ProjectConsortiumInstituteDTO;
import org.gentar.EntityMapper;
import org.springframework.stereotype.Component;
import org.gentar.organization.institute.Institute;
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
//        Institute institute = entityMapper.toTarget(projectConsortiumInstituteDTO, Institute.class);

        String instituteName = projectConsortiumInstituteDTO.getInstituteName();
        Institute institute = instituteService.getInstituteByNameOrThrowException(instituteName);
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
