package org.gentar.biology.plan.attempt.phenotyping.stage;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.util.JsonConverter;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

class PhenotypingStageCommonDTOTest
{
    @Test
    public void testStructure() throws JsonProcessingException
    {
        PhenotypingStageCommonDTO phenotypingStageCommonDTO = new PhenotypingStageCommonDTO();
        phenotypingStageCommonDTO.setPhenotypingExperimentsStarted(LocalDate.parse("2020-06-17"));
        phenotypingStageCommonDTO.setInitialDataReleaseDate(LocalDate.parse("2020-06-17"));

        TissueDistributionDTO tissueDistributionDTO = new TissueDistributionDTO();
        phenotypingStageCommonDTO.setTissueDistributionCentreDTOs(List.of(tissueDistributionDTO));

        String json = JsonConverter.toJson(phenotypingStageCommonDTO);
        assertThat(json, is(notNullValue()));
        assertThat(json, is("{\"phenotypingExperimentsStarted\":\"2020-06-17\"," +
                "\"initialDataReleaseDate\":\"2020-06-17\"," +
                "\"tissueDistributions\":[{\"id\":null,\"startDate\":null,\"endDate\":null," +
                "\"workUnitName\":null,\"materialDepositedTypeName\":null}]}"));
    }

}