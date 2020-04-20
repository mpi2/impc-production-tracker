package org.gentar.biology.plan.attempt.phenotyping;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.outcome.OutcomeMapper;
import org.gentar.biology.strain.StrainMapper;
import org.gentar.biology.plan.starting_point.PlanStartingPoint;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PhenotypingAttemptMapper implements Mapper<PhenotypingAttempt, PhenotypingAttemptDTO>
{
    private EntityMapper entityMapper;
    private StrainMapper strainMapper;
    private OutcomeMapper outcomeMapper;
    private PhenotypingStageMapper phenotypingStageMapper;

    public PhenotypingAttemptMapper(
        EntityMapper entityMapper,
        StrainMapper strainMapper,
        OutcomeMapper outcomeMapper,
        PhenotypingStageMapper phenotypingStageMapper)
    {
        this.entityMapper = entityMapper;
        this.strainMapper = strainMapper;
        this.outcomeMapper = outcomeMapper;
        this.phenotypingStageMapper = phenotypingStageMapper;
    }

    @Override
    public PhenotypingAttemptDTO toDto(PhenotypingAttempt entity)
    {
        PhenotypingAttemptDTO phenotypingAttemptDTO =
            entityMapper.toTarget(entity, PhenotypingAttemptDTO.class);
        phenotypingAttemptDTO.setStrainName(strainMapper.toDto(entity.getStrain()));

        Set<PlanStartingPoint> planStartingPoints = entity.getPlan().getPlanStartingPoints();
        if (planStartingPoints.size() == 1)
        {
            PlanStartingPoint planStartingPoint = planStartingPoints.iterator().next();
            Outcome outcome = planStartingPoint.getOutcome();
            phenotypingAttemptDTO.setOutcomeDTO(outcomeMapper.toDto(outcome));
        }

        phenotypingAttemptDTO.setPhenotypingStageDTOs(phenotypingStageMapper.toDtos(entity.getPhenotypingStages()));
        return phenotypingAttemptDTO;
    }

    @Override
    public PhenotypingAttempt toEntity(PhenotypingAttemptDTO dto)
    {
        PhenotypingAttempt phenotypingAttempt = entityMapper.toTarget(dto, PhenotypingAttempt.class);

        return phenotypingAttempt;
    }
}
