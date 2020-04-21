package org.gentar.biology.plan.attempt.crispr;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.crispr.reagent.CrisprAttemptReagent;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CrisprAttemptReagentMapper implements Mapper<CrisprAttemptReagent, CrisprAttemptReagentDTO>
{
    private EntityMapper entityMapper;
    private ReagentMapper reagentMapper;

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
        CrisprAttemptReagent crisprAttemptReagent = entityMapper.toTarget(crisprAttemptReagentDTO, CrisprAttemptReagent.class);
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
