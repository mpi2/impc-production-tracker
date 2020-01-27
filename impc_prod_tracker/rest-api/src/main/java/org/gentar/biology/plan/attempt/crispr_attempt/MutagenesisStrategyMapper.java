package org.gentar.biology.plan.attempt.crispr_attempt;

import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.crispr.mutagenesis_strategy.MutagenesisStrategy;
import org.gentar.biology.plan.attempt.crispr.mutagenesis_strategy.MutagenesisStrategyProperty;
import org.gentar.biology.plan.attempt.crispr.mutagenesis_strategy.MutagenesisStrategyService;
import org.gentar.biology.plan.attempt.crispr.mutagenesis_strategy.MutagenesisStrategyType;
import org.gentar.biology.plan.production.crispr_attempt.MutagenesisStrategyDTO;
import org.springframework.stereotype.Component;

@Component
public class MutagenesisStrategyMapper implements Mapper<MutagenesisStrategy, MutagenesisStrategyDTO>
{
    private MutagenesisStrategyService mutagenesisStrategyService;

    public MutagenesisStrategyMapper(MutagenesisStrategyService mutagenesisStrategyService)
    {
        this.mutagenesisStrategyService = mutagenesisStrategyService;
    }

    @Override
    public MutagenesisStrategyDTO toDto(MutagenesisStrategy entity)
    {
        MutagenesisStrategyDTO mutagenesisStrategyDTO = new MutagenesisStrategyDTO();
        if (entity != null)
        {
            mutagenesisStrategyDTO.setId(entity.getId());
            MutagenesisStrategyType mutagenesisStrategyType = entity.getMutagenesisStrategyType();
            MutagenesisStrategyProperty mutagenesisStrategyProperty =
                entity.getMutagenesisStrategyProperty();
            if (mutagenesisStrategyType != null)
            {
                mutagenesisStrategyDTO.setTypeName(mutagenesisStrategyType.getName());
            }
            if (mutagenesisStrategyProperty != null)
            {
                mutagenesisStrategyDTO.setPropertyName(mutagenesisStrategyProperty.getName());
            }
        }
        return mutagenesisStrategyDTO;
    }

    @Override
    public MutagenesisStrategy toEntity(MutagenesisStrategyDTO dto)
    {
        MutagenesisStrategy mutagenesisStrategy = new MutagenesisStrategy();
        if (dto != null)
        {
            mutagenesisStrategy.setId(dto.getId());
            mutagenesisStrategy.setMutagenesisStrategyType(
                mutagenesisStrategyService.getMutagenesisStrategyTypeByName(dto.getTypeName()));
            mutagenesisStrategy.setMutagenesisStrategyProperty(
                mutagenesisStrategyService.mutagenesisStrategyPropertyByName(dto.getPropertyName()));
        }
        return mutagenesisStrategy;
    }
}
