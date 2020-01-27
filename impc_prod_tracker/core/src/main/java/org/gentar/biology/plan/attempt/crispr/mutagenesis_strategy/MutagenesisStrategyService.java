package org.gentar.biology.plan.attempt.crispr.mutagenesis_strategy;

import org.springframework.stereotype.Component;

@Component
public class MutagenesisStrategyService
{
    private MutagenesisStrategyPropertyRepository mutagenesisStrategyPropertyRepository;
    private MutagenesisStrategyTypeRepository mutagenesisStrategyTypeRepository;

    public MutagenesisStrategyService(
        MutagenesisStrategyPropertyRepository mutagenesisStrategyPropertyRepository,
        MutagenesisStrategyTypeRepository mutagenesisStrategyTypeRepository)
    {
        this.mutagenesisStrategyPropertyRepository = mutagenesisStrategyPropertyRepository;
        this.mutagenesisStrategyTypeRepository = mutagenesisStrategyTypeRepository;
    }

    public MutagenesisStrategyProperty mutagenesisStrategyPropertyByName(String name)
    {
        return mutagenesisStrategyPropertyRepository.findByNameIgnoreCase(name);
    }

    public MutagenesisStrategyType getMutagenesisStrategyTypeByName(String name)
    {
        return mutagenesisStrategyTypeRepository.findByNameIgnoreCase(name);
    }
}
