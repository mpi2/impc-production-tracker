package org.gentar.organization.consortium;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsortiumServiceImpl implements ConsortiumService
{
    private ConsortiumRepository consortiumRepository;

    private static final String CONSORTIUM_NOT_EXISTS_ERROR = "Consortium %s does not exist.";

    public ConsortiumServiceImpl(ConsortiumRepository consortiumRepository)
    {
        this.consortiumRepository = consortiumRepository;
    }

    @Cacheable("consortiumNames")
    public Consortium findConsortiumByName(String name)
    {
        return consortiumRepository.findByNameIgnoreCase(name);
    }

    public Consortium getConsortiumByNameOrThrowException(String consortiumName)
    {
        Consortium consortium = findConsortiumByName(consortiumName);
        if (consortium == null)
        {
            throw new UserOperationFailedException(
                    String.format(CONSORTIUM_NOT_EXISTS_ERROR, consortiumName));
        }
        return consortium;
    }

    public List<Consortium> findAllConsortia()
    {
        return consortiumRepository.findAll();
    }
}
