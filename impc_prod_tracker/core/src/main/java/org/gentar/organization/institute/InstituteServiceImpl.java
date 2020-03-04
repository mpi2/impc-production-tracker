package org.gentar.organization.institute;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class InstituteServiceImpl implements InstituteService
{
    private InstituteRepository instituteRepository;

    private static final String INSTITUTE_NOT_EXISTS_ERROR = "Institute '%s' does not exist.";

    public InstituteServiceImpl(InstituteRepository instituteRepository)
    {
        this.instituteRepository = instituteRepository;
    }

    @Cacheable("instituteNames")
    public Institute findInstituteByName(String name)
    {
        return instituteRepository.findByNameIgnoreCase(name);
    }

    public Institute getInstituteByNameOrThrowException(String instituteName)
    {
        Institute institute = findInstituteByName(instituteName);
        if (institute == null)
        {
            throw new UserOperationFailedException(
                    String.format(INSTITUTE_NOT_EXISTS_ERROR, instituteName));
        }
        return institute;
    }

    public List<Institute> getAllInstitutes()
    {
        return instituteRepository.findAll();
    }
}
