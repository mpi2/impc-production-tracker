package org.gentar.biology.strain;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class StrainServiceImpl implements StrainService
{
    private StrainRepository strainRepository;
    private static final String STRAIN_NOT_FOUND_ERROR = "Strain '%s' does not exist.";

    public StrainServiceImpl (StrainRepository strainRepository)
    {
        this.strainRepository = strainRepository;
    }

    @Override
    public Strain getStrainByName (String name) { return strainRepository.findByNameIgnoreCase(name); }

    @Override
    public Strain getStrainByNameFailWhenNotFound(String name)
    {
        Strain strain = getStrainByName(name);
        if (strain == null)
        {
            throw new UserOperationFailedException(String.format(STRAIN_NOT_FOUND_ERROR, name));
        }
        return strain;
    }

    @Override
    public Strain getStrainById (Long id) { return strainRepository.findById(id).orElse(null); }
}
