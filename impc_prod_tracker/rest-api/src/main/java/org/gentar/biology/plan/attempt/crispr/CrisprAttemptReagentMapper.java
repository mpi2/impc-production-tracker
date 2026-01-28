package org.gentar.biology.plan.attempt.crispr;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.crispr.reagent.CrisprAttemptReagent;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CrisprAttemptReagentMapper implements Mapper<CrisprAttemptReagent, CrisprAttemptReagentDTO>
{
    private final EntityMapper entityMapper;
    private final ReagentMapper reagentMapper;

    public CrisprAttemptReagentMapper(EntityMapper entityMapper, ReagentMapper reagentMapper)
    {
        this.entityMapper = entityMapper;
        this.reagentMapper = reagentMapper;
    }

    @Override
    public CrisprAttemptReagentDTO toDto(CrisprAttemptReagent crisprAttemptReagent) {
        CrisprAttemptReagentDTO crisprAttemptReagentDTO = entityMapper.toTarget(crisprAttemptReagent, CrisprAttemptReagentDTO.class);
        crisprAttemptReagentDTO.setReagentName(crisprAttemptReagent.getReagent().getName());
        crisprAttemptReagentDTO.setReagentDescription(crisprAttemptReagent.getReagent().getDescription());
        return crisprAttemptReagentDTO;
    }

    @Override
    public List<CrisprAttemptReagentDTO> toDtos(Collection<CrisprAttemptReagent> crisprAttemptReagents) {
        List<CrisprAttemptReagentDTO> crisprAttemptReagentDTOS = new ArrayList<>();
        if (crisprAttemptReagents != null)
        {
            crisprAttemptReagents.forEach(crisprAttemptReagent -> crisprAttemptReagentDTOS.add(toDto(crisprAttemptReagent)));
        }
        return crisprAttemptReagentDTOS;
    }

    @Override
    public CrisprAttemptReagent toEntity(CrisprAttemptReagentDTO crisprAttemptReagentDTO) {
        if (crisprAttemptReagentDTO == null)
        {
            return null;
        }
        
        CrisprAttemptReagent crisprAttemptReagent = new CrisprAttemptReagent();
        
        if (crisprAttemptReagentDTO.getId() != null && crisprAttemptReagentDTO.getId() > 0)
        {
            crisprAttemptReagent.setId(crisprAttemptReagentDTO.getId());
        }

        if (crisprAttemptReagentDTO.getConcentration() != null)
        {
            crisprAttemptReagent.setConcentration(crisprAttemptReagentDTO.getConcentration().intValue());
        }
        crisprAttemptReagent.setReagent(reagentMapper.toEntity(crisprAttemptReagentDTO.getReagentName()));
        
        return crisprAttemptReagent;
    }

    @Override
    public Set<CrisprAttemptReagent> toEntities(Collection<CrisprAttemptReagentDTO> crisprAttemptReagentDTOS) {
        Set<CrisprAttemptReagent> crisprAttemptReagents = new HashSet<>();
        if (crisprAttemptReagentDTOS != null)
        {
            crisprAttemptReagentDTOS.forEach(crisprAttemptReagentDTO -> crisprAttemptReagents.add(toEntity(crisprAttemptReagentDTO)));
        }
        return crisprAttemptReagents;
    }
}
