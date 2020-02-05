package org.gentar.biology.plan.attempt.crispr_attempt;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.crispr.reagent.Reagent;
import org.gentar.biology.plan.production.crispr_attempt.ReagentDTO;
import org.springframework.stereotype.Component;

@Component
public class ReagentMapper implements Mapper<Reagent, ReagentDTO> {

    private EntityMapper entityMapper;

    public ReagentMapper(EntityMapper entityMapper) {
        this.entityMapper = entityMapper;
    }

    @Override
    public ReagentDTO toDto(Reagent reagent) {
        ReagentDTO reagentDTO =  entityMapper.toTarget(reagent, ReagentDTO.class);
        return reagentDTO;
    }

    @Override
    public Reagent toEntity(ReagentDTO reagentDTO) {
        Reagent reagent = entityMapper.toTarget(reagentDTO, Reagent.class);
        return reagent;
    }
}
