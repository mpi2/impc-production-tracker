package org.gentar.biology.project.consortium.institute;

import org.gentar.Mapper;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.organization.institute.InstituteService;
import org.springframework.stereotype.Component;
import org.gentar.organization.institute.Institute;
import java.util.*;

@Component
public class ProjectConsortiumInstituteMapper implements Mapper<Institute, String>
{
    private InstituteService instituteService;

    private static final String INSTITUTE_NOT_FOUND_ERROR = "Institute name '%s' does not exist.";

    public ProjectConsortiumInstituteMapper(InstituteService instituteService)
    {
        this.instituteService = instituteService;
    }

    public Institute toEntity(String projectConsortiumInstitute)
    {
        Institute institute = instituteService.getInstituteByNameOrThrowException(projectConsortiumInstitute);

        if (institute == null)
        {
            throw new UserOperationFailedException(String.format(INSTITUTE_NOT_FOUND_ERROR, institute));
        }

        return institute;
    }

    public Set<Institute> toEntities(Collection<String> projectConsortiumInstitutes)
    {
        Set<Institute> institutes = new HashSet<>();
        if (projectConsortiumInstitutes != null)
        {
            projectConsortiumInstitutes.forEach(projectConsortiumInstitute -> institutes.add(toEntity(projectConsortiumInstitute)));
        }
        return institutes;
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

    public List<String> toDtos(Collection<Institute> institutes)
    {
        List<String> instituteNames = new ArrayList<>();
        if (institutes != null)
        {
            institutes.forEach(x -> instituteNames.add(toDto(x)));
        }
        return instituteNames;
    }
}
