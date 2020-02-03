package org.gentar.biology.outcome;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutcomeServiceImpl implements OutcomeService
{
    private OutcomeRepository outcomeRepository;
    private OutcomeTypeRepository outcomeTypeRepository;
    private OutcomeCreator outcomeCreator;

    public OutcomeServiceImpl(
        OutcomeRepository outcomeRepository,
        OutcomeTypeRepository outcomeTypeRepository,
        OutcomeCreator outcomeCreator)
    {
        this.outcomeRepository = outcomeRepository;
        this.outcomeTypeRepository = outcomeTypeRepository;
        this.outcomeCreator = outcomeCreator;
    }

    public List<Outcome> findAll()
    {
        return outcomeRepository.findAll();
    }

    public Outcome create(Outcome outcome)
    {
        return outcomeCreator.create(outcome);
    }

    public Outcome update(Outcome outcome)
    {
        System.out.println("OutcomeService Update " + outcome);
        return null;
    }

    @Override
    public OutcomeType getOutcomeTypeByName(String name)
    {
        return outcomeTypeRepository.findByNameIgnoreCase(name);
    }

    @Override
    public OutcomeType getOutcomeTypeByNameFailingWhenNull(String name)
    {
        OutcomeType outcomeType = getOutcomeTypeByName(name);
        if (outcomeType == null)
        {
            throw new UserOperationFailedException("Outcome type" + name + " does not exist.");
        }
        return outcomeType;
    }
}
