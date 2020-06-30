package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.biology.plan.attempt.phenotyping.stage.material_deposited_type.MaterialDepositedType;
import org.gentar.biology.plan.attempt.phenotyping.stage.tissue_distribution.TissueDistribution;
import org.gentar.organization.work_unit.WorkUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PhenotypingStageCommonMapperTest
{
    private PhenotypingStageCommonMapper testInstance;

    @Mock
    private TissueDistributionMapper tissueDistributionMapper;

    @BeforeEach
    void setUp()
    {
        testInstance = new PhenotypingStageCommonMapper(tissueDistributionMapper);
    }

    @Test
    void toDto()
    {
        PhenotypingStage phenotypingStage = new PhenotypingStage();
        phenotypingStage.setPhenotypingExperimentsStarted(LocalDate.parse("2020-01-01"));
        phenotypingStage.setDoNotCountTowardsCompleteness(true);
        phenotypingStage.setInitialDataReleaseDate(LocalDate.parse("2020-02-02"));

        TissueDistribution tissueDistribution = new TissueDistribution();
        WorkUnit workUnit = new WorkUnit();
        workUnit.setName("workUnitName");
        tissueDistribution.setWorkUnit(workUnit);
        MaterialDepositedType materialDepositedType = new MaterialDepositedType();
        materialDepositedType.setName("depositedMaterialName");
        tissueDistribution.setMaterialDepositedType(materialDepositedType);
        tissueDistribution.setStartDate(LocalDate.parse("2020-03-03"));
        Set<TissueDistribution> tissueDistributions = new HashSet<>();
        tissueDistributions.add(tissueDistribution);
        phenotypingStage.setTissueDistributions(tissueDistributions);

        PhenotypingStageCommonDTO phenotypingStageCommonDTO = testInstance.toDto(phenotypingStage);

        verify(tissueDistributionMapper, times(1)).toDtos(phenotypingStage.getTissueDistributions());

        assertThat(phenotypingStageCommonDTO.getPhenotypingExperimentsStarted(), is(LocalDate.parse("2020-01-01")));
        assertThat(phenotypingStageCommonDTO.getDoNotCountTowardsCompleteness(), is(true));
        assertThat(phenotypingStageCommonDTO.getInitialDataReleaseDate(), is(LocalDate.parse("2020-02-02")));
    }

    @Test
    void toEntity()
    {
        PhenotypingStageCommonDTO phenotypingStageCommonDTO = new PhenotypingStageCommonDTO();
        phenotypingStageCommonDTO.setPhenotypingExperimentsStarted(LocalDate.parse("2020-01-01"));
        phenotypingStageCommonDTO.setDoNotCountTowardsCompleteness(true);
        phenotypingStageCommonDTO.setInitialDataReleaseDate(LocalDate.parse("2020-02-02"));

        PhenotypingStage phenotypingStage = testInstance.toEntity(phenotypingStageCommonDTO);

        verify(tissueDistributionMapper, times(1)).toEntities(
                phenotypingStageCommonDTO.getTissueDistributionCentreDTOs());
        assertThat(phenotypingStage, is(notNullValue()));
        assertThat(phenotypingStage.getDoNotCountTowardsCompleteness(), is(true));
        assertThat(phenotypingStage.getPhenotypingExperimentsStarted(), is(LocalDate.parse("2020-01-01")));
        assertThat(phenotypingStage.getInitialDataReleaseDate(), is(LocalDate.parse("2020-02-02")));
    }
}