package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.phenotyping.stage.tissue_distribution.TissueDistribution;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PhenotypingStageCommonMapper implements Mapper<PhenotypingStage, PhenotypingStageCommonDTO>
{
    private TissueDistributionMapper tissueDistributionMapper;

    public PhenotypingStageCommonMapper(TissueDistributionMapper tissueDistributionMapper) {
        this.tissueDistributionMapper = tissueDistributionMapper;
    }

    @Override
    public PhenotypingStageCommonDTO toDto(PhenotypingStage phenotypingStage) {
        PhenotypingStageCommonDTO phenotypingStageCommonDTO = new PhenotypingStageCommonDTO();
        phenotypingStageCommonDTO.setPhenotypingExperimentsStarted(phenotypingStage.getPhenotypingExperimentsStarted());
        phenotypingStageCommonDTO.setDoNotCountTowardsCompleteness(phenotypingStage.getDoNotCountTowardsCompleteness());
        phenotypingStageCommonDTO.setInitialDataReleaseDate(phenotypingStage.getInitialDataReleaseDate());
        phenotypingStageCommonDTO.setTissueDistributionCentreDTOs(
                tissueDistributionMapper.toDtos(phenotypingStage.getTissueDistributions()));
        return phenotypingStageCommonDTO;
    }

    @Override
    public PhenotypingStage toEntity(PhenotypingStageCommonDTO dto) {
        PhenotypingStage phenotypingStage = new PhenotypingStage();
        phenotypingStage.setPhenotypingExperimentsStarted(dto.getPhenotypingExperimentsStarted());
        phenotypingStage.setDoNotCountTowardsCompleteness(dto.getDoNotCountTowardsCompleteness());
        phenotypingStage.setInitialDataReleaseDate(dto.getInitialDataReleaseDate());
        setTissueDistributionCentre(phenotypingStage, dto);
        return phenotypingStage;
    }

    private void setTissueDistributionCentre(
            PhenotypingStage phenotypingStage, PhenotypingStageCommonDTO phenotypingStageDTO)
    {
        Set<TissueDistribution> tissueDistributions =
                tissueDistributionMapper.toEntities(phenotypingStageDTO.getTissueDistributionCentreDTOs());
        tissueDistributions.forEach(x -> x.setPhenotypingStage(phenotypingStage));
        phenotypingStage.setTissueDistributions(tissueDistributions);
    }
}
