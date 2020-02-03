package org.gentar.biology.outcome;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutcomeServiceImpl implements OutcomeService
{
    private OutcomeRepository outcomeRepository;

    public OutcomeServiceImpl(OutcomeRepository outcomeRepository)
    {
        this.outcomeRepository = outcomeRepository;
    }

    public List<Outcome> findAll()
    {
        return outcomeRepository.findAll();
    }

    public Outcome create(Outcome outcome)
    {
        return null;
    }

    public Outcome update(Outcome outcome)
    {
        System.out.println("OutcomeService Update " + outcome);
        return null;
    }
}
