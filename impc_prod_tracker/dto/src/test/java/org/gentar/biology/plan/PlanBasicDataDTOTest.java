package org.gentar.biology.plan;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.gentar.biology.plan.attempt.breeding.BreedingAttemptDTO;
import org.gentar.biology.plan.attempt.es_cell_allele_modification.EsCellAlleleModificationAttemptDTO;
import org.gentar.biology.plan.attempt.crispr.CrisprAttemptDTO;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttemptCreationDTO;
import org.gentar.biology.plan.plan_starting_point.PlanStartingPointDTO;
import org.junit.jupiter.api.Test;
import org.util.JsonConverter;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

class PlanBasicDataDTOTest
{
    @Test
    public void testCrisprPlanBasicDataDTO() throws JsonProcessingException
    {
        PlanBasicDataDTO planBasicDataDTO = new PlanBasicDataDTO();
        planBasicDataDTO.setPlanCommonDataDTO(new PlanCommonDataDTO());
        CrisprAttemptDTO crisprAttemptDTO = new CrisprAttemptDTO();
        planBasicDataDTO.setCrisprAttemptDTO(crisprAttemptDTO);

        String json = JsonConverter.toJson(planBasicDataDTO);
        assertThat(json, is(notNullValue()));
        assertThat(json, is("{\"funderNames\":null,\"workUnitName\":null,\"workGroupName\":null,\"comment\":null," +
                "\"crisprAttempt\":{\"miDate\":null,\"experimental\":null,\"comment\":null,\"mutagenesisExternalRef\":null," +
                "\"attemptExternalRef\":null,\"embryoTransferDay\":null,\"totalTransferred\":null,\"nucleases\":null," +
                "\"guides\":null,\"mutagenesisDonors\":null,\"reagents\":null,\"genotypePrimers\":null," +
                "\"totalEmbryosInjected\":null,\"totalEmbryosSurvived\":null,\"embryo2Cell\":null,\"assay\":null," +
                "\"strainInjectedName\":null}}"));
    }

    @Test
    public void testBreedingPlanBasicDataDTO() throws JsonProcessingException
    {
        PlanBasicDataDTO planBasicDataDTO = new PlanBasicDataDTO();
        planBasicDataDTO.setPlanCommonDataDTO(new PlanCommonDataDTO());
        BreedingAttemptDTO breedingAttemptDTO = new BreedingAttemptDTO();
        planBasicDataDTO.setBreedingAttemptDTO(breedingAttemptDTO);
        planBasicDataDTO.setPlanStartingPointDTOS(Collections.singletonList(new PlanStartingPointDTO()));

        String json = JsonConverter.toJson(planBasicDataDTO);
        assertThat(json, is(notNullValue()));
        assertThat(json, is("{\"funderNames\":null,\"workUnitName\":null,\"workGroupName\":null,\"comment\":null," +
                "\"breedingStartingPoints\":[{\"links\":[],\"outcomeTpo\":null," +
                "\"productionPlanPin\":null}],\"breedingAttempt\":{\"numberOfCreMatingsStarted\":null," +
                "\"numberOfCreMatingsSuccessful\":null,\"creExcesion\":null,\"tatCre\":null,\"deleterStrainName\":null}}"));
    }

    @Test
    public void testEsCellAlleleModificationPlanBasicDataDTO() throws JsonProcessingException
    {
        PlanBasicDataDTO planBasicDataDTO = new PlanBasicDataDTO();
        planBasicDataDTO.setPlanCommonDataDTO(new PlanCommonDataDTO());
        EsCellAlleleModificationAttemptDTO esCellAlleleModificationAttemptDTO = new EsCellAlleleModificationAttemptDTO();
        planBasicDataDTO.setEsCellAlleleModificationAttemptDTO(esCellAlleleModificationAttemptDTO);
        planBasicDataDTO.setModificationPlanStartingPointDTO(new PlanStartingPointDTO());

        String json = JsonConverter.toJson(planBasicDataDTO);
        assertThat(json, is(notNullValue()));
        assertThat(json, is("{\"funderNames\":null,\"workUnitName\":null,\"workGroupName\":null,\"comment\":null," +
                "\"esCellAlleleModificationStartingPoint\":{\"links\":[],\"outcomeTpo\":null," +
                "\"productionPlanPin\":null},\"esCellAlleleModificationAttempt\":{\"modificationExternalRef\":null," +
                "\"numberOfCreMatingsSuccessful\":null,\"tatCre\":null,\"deleterStrainName\":null}}"));
    }

    @Test
    public void testPhenotypingPlanBasicDataDTO() throws JsonProcessingException
    {
        PlanBasicDataDTO planBasicDataDTO = new PlanBasicDataDTO();
        planBasicDataDTO.setPlanCommonDataDTO(new PlanCommonDataDTO());
        PhenotypingAttemptCreationDTO phenotypingAttemptCreationDTO = new PhenotypingAttemptCreationDTO();
        planBasicDataDTO.setPhenotypingAttemptCreationDTO(phenotypingAttemptCreationDTO);
        planBasicDataDTO.setPlanStartingPointDTO(new PlanStartingPointDTO());

        String json = JsonConverter.toJson(planBasicDataDTO);

        assertThat(json, is(notNullValue()));
        assertThat(json, is("{\"funderNames\":null,\"workUnitName\":null,\"workGroupName\":null," +
            "\"comment\":null," +
            "\"phenotypingStartingPoint\":{\"links\":[],\"outcomeTpo\":null,\"productionPlanPin\":null}," +
            "\"phenotypingAttempt\":{\"phenotypingStages\":null}}"));
    }
}
