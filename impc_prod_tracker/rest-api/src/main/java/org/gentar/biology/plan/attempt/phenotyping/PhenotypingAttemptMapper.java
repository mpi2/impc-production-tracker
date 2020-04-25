package org.gentar.biology.plan.attempt.phenotyping;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
<<<<<<< Updated upstream
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.outcome.OutcomeMapper;
import org.gentar.biology.strain.StrainMapper;
import org.gentar.biology.plan.starting_point.PlanStartingPoint;
=======
>>>>>>> Stashed changes
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.strain.Strain;
import org.gentar.biology.strain.StrainMapper;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PhenotypingAttemptMapper implements Mapper<PhenotypingAttempt, PhenotypingAttemptDTO>
{
    private EntityMapper entityMapper;
    private StrainMapper strainMapper;

    private PhenotypingStageMapper phenotypingStageMapper;

    public PhenotypingAttemptMapper(
        EntityMapper entityMapper,
        StrainMapper strainMapper,
        PhenotypingStageMapper phenotypingStageMapper)
    {
        this.entityMapper = entityMapper;
        this.strainMapper = strainMapper;
        this.phenotypingStageMapper = phenotypingStageMapper;
    }

    @Override
    public PhenotypingAttemptDTO toDto(PhenotypingAttempt phenotypingAttempt)
    {
        PhenotypingAttemptDTO phenotypingAttemptDTO = null;
        if (phenotypingAttempt != null)
        {
            phenotypingAttemptDTO = entityMapper.toTarget(phenotypingAttempt, PhenotypingAttemptDTO.class);
            phenotypingAttemptDTO.setStrainName(strainMapper.toDto(phenotypingAttempt.getStrain()));
            phenotypingAttemptDTO.setPhenotypingStageDTOs(phenotypingStageMapper.toDtos(phenotypingAttempt.getPhenotypingStages()));
        }
        return phenotypingAttemptDTO;
    }

    @Override
    public PhenotypingAttempt toEntity(PhenotypingAttemptDTO dto)
    {
        PhenotypingAttempt phenotypingAttempt = entityMapper.toTarget(dto, PhenotypingAttempt.class);
        setStrain(phenotypingAttempt, dto);
<<<<<<< Updated upstream
=======
        //TODO add stages
>>>>>>> Stashed changes
        setPhenotypingStagesToEntity(phenotypingAttempt, dto);

        return phenotypingAttempt;
    }

    private void setPhenotypingStagesToEntity(PhenotypingAttempt phenotypingAttempt, PhenotypingAttemptDTO dto)
    {
        Set<PhenotypingStage> phenotypingStages = phenotypingStageMapper.toEntities(dto.getPhenotypingStageDTOs());
        phenotypingStages.forEach(x -> x.setPhenotypingAttempt(phenotypingAttempt));

        phenotypingAttempt.setPhenotypingStages(phenotypingStages);
    }

    private void setStrain(PhenotypingAttempt phenotypingAttempt, PhenotypingAttemptDTO dto)
    {
        Strain strain = strainMapper.toEntity(dto.getStrainName());
        phenotypingAttempt.setStrain(strain);
    }
}
