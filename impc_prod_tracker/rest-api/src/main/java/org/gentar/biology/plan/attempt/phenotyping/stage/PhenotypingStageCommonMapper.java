package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.phenotyping.stage.tissue_distribution.TissueDistribution;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PhenotypingStageCommonMapper implements Mapper<PhenotypingStage, PhenotypingStageCommonDTO>
{
    private EntityMapper entityMapper;
    private TissueDistributionMapper tissueDistributionMapper;

    public PhenotypingStageCommonMapper(EntityMapper entityMapper, TissueDistributionMapper tissueDistributionMapper)
    {
        this.entityMapper = entityMapper;
        this.tissueDistributionMapper = tissueDistributionMapper;
    }

    @Override
    public PhenotypingStageCommonDTO toDto(PhenotypingStage phenotypingStage) {
        PhenotypingStageCommonDTO phenotypingStageCommonDTO = entityMapper.toTarget(phenotypingStage,
                PhenotypingStageCommonDTO.class);
        phenotypingStageCommonDTO.setTissueDistributionCentreDTOs(
                tissueDistributionMapper.toDtos(phenotypingStage.getTissueDistributions()));
        return phenotypingStageCommonDTO;
    }

    @Override
    public PhenotypingStage toEntity(PhenotypingStageCommonDTO dto) {
        PhenotypingStage phenotypingStage =
                entityMapper.toTarget(dto, PhenotypingStage.class);
        setTissueDistributionCentre(phenotypingStage, dto);
        return phenotypingStage;
    }

    private void setTissueDistributionCentre (
            PhenotypingStage phenotypingStage, PhenotypingStageCommonDTO phenotypingStageCommonDTO)
    {
        Set<TissueDistribution> tissueDistributions =
                tissueDistributionMapper.toEntities(phenotypingStageCommonDTO.getTissueDistributionCentreDTOs());
        tissueDistributions.forEach(x -> x.setPhenotypingStage(phenotypingStage));
        phenotypingStage.setTissueDistributions(tissueDistributions);
    }
}
