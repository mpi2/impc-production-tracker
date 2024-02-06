package org.gentar.biology.project.consortium.institute;

import org.gentar.Mapper;
import org.gentar.organization.institute.InstituteService;
import org.springframework.stereotype.Component;
import org.gentar.organization.institute.Institute;

@Component
public class ProjectConsortiumInstituteMapper implements Mapper<Institute, String>
{
    private final InstituteService instituteService;

    public ProjectConsortiumInstituteMapper(InstituteService instituteService)
    {
        this.instituteService = instituteService;
    }

    public Institute toEntity(String instituteName)
    {
        return instituteService.getInstituteByNameOrThrowException(instituteName);
    }

    public String toDto(Institute institute)
    {
        String name = null;
        if (institute != null)
        {
            name = institute.getName();
        }
        return name;
    }
}
