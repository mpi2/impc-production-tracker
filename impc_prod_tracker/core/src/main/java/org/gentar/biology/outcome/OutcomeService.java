package org.gentar.biology.outcome;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutcomeService
{
    private OutcomeRepository outcomeRepository;

    public OutcomeService(OutcomeRepository outcomeRepository)
    {
        this.outcomeRepository = outcomeRepository;
    }

    public List<Outcome> findAll()
    {
        return outcomeRepository.findAll();
    }
}
