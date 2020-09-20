package org.gentar.organization.consortium;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConsortiumServiceImpl implements ConsortiumService
{
    private final ConsortiumRepository consortiumRepository;

    private static final String CONSORTIUM_NOT_EXISTS_ERROR = "Consortium '%s' does not exist.";
    private static final String CONSORTIUM_NULL_ERROR = "Consortium cannot be null. Select at least one.";

    public ConsortiumServiceImpl(ConsortiumRepository consortiumRepository)
    {
        this.consortiumRepository = consortiumRepository;
    }

    @Override
    @Cacheable("consortiumNames")
    public Consortium findConsortiumByName(String name)
    {
        return consortiumRepository.findByNameIgnoreCase(name);
    }

    @Override
    public Consortium getConsortiumByNameOrThrowException(String consortiumName)
    {
        if (consortiumName == null)
        {
            throw new UserOperationFailedException(
                    String.format(CONSORTIUM_NULL_ERROR));
        }
        Consortium consortium = findConsortiumByName(consortiumName);
        if (consortium == null)
        {
            throw new UserOperationFailedException(
                String.format(CONSORTIUM_NOT_EXISTS_ERROR, consortiumName));
        }
        return consortium;
    }

    @Override
    public void getErrorConsortiumNull()
    {
        throw new UserOperationFailedException(
                String.format(CONSORTIUM_NULL_ERROR));
    }

    @Override
    public List<Consortium> findAllConsortia()
    {
        return consortiumRepository.findAll();
    }

    @Override
    /**
     * In the future this list should be created from a query in the database. Currently it's
     * fixed and only have IMPC as consortium whom abbreviation can be used to construct the
     * symbol of an allele.
     */
    public List<String> getConsortiaNamesUsableToConstructSymbols()
    {
        List<String> consortiaToConstructSymbols = new ArrayList<>();
        // Make sure the consortium exists in database
        Consortium impc = consortiumRepository.findByNameIgnoreCase("IMPC");
        consortiaToConstructSymbols.add(impc.getName());
        return consortiaToConstructSymbols;
    }
}
