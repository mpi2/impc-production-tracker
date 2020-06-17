package org.gentar.biology.plan.attempt.phenotyping.stage;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.util.JsonConverter;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

class PhenotypingStageCreationDTOTest
{
    @Test
    public void testStructure() throws JsonProcessingException
    {
        PhenotypingStageCreationDTO phenotypingStageCreationDTO = new PhenotypingStageCreationDTO();
        PhenotypingStageCommonDTO phenotypingStageCommonDTO = new PhenotypingStageCommonDTO();
        phenotypingStageCommonDTO.setPhenotypingExperimentsStarted(LocalDate.parse("2020-06-17"));
        phenotypingStageCommonDTO.setDoNotCountTowardsCompleteness(true);
        phenotypingStageCommonDTO.setInitialDataReleaseDate(LocalDate.parse("2020-06-17"));
        phenotypingStageCreationDTO.setPhenotypingStageCommonDTO(phenotypingStageCommonDTO);
        phenotypingStageCreationDTO.setPhenotypingTypeName("phenotypingTypeName");

        String json = JsonConverter.toJson(phenotypingStageCreationDTO);
        assertThat(json, is(notNullValue()));
        assertThat(json, is("{\"phenotypingTypeName\":\"phenotypingTypeName\"," +
                "\"phenotypingExperimentsStarted\":\"2020-06-17\",\"doNotCountTowardsCompleteness\":true," +
                "\"initialDataReleaseDate\":\"2020-06-17\",\"tissueDistributions\":null}"));
    }

}