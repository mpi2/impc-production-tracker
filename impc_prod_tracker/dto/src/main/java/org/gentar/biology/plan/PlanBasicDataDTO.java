package org.gentar.biology.plan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.gentar.biology.plan.attempt.breeding.BreedingAttemptDTO;
import org.gentar.biology.plan.attempt.crispr_allele_modification.CrisprAlleleModificationAttemptDTO;
import org.gentar.biology.plan.attempt.es_cell_allele_modification.EsCellAlleleModificationAttemptDTO;
import org.gentar.biology.plan.attempt.crispr.CrisprAttemptDTO;
import org.gentar.biology.plan.attempt.es_cell.EsCellAttemptDTO;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttemptCreationDTO;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttemptResponseDTO;
import org.gentar.biology.plan.plan_starting_point.PlanStartingPointDTO;
import java.util.List;

@Data
public class PlanBasicDataDTO
{
    @JsonIgnore
    // Internal id useful keep the id accessible in all the plan DTOS.
    private Long id;

    // Common information for all plan dtos.
    @JsonUnwrapped
    private PlanCommonDataDTO planCommonDataDTO;

    // Starting point outcome for phenotyping plans
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("phenotypingStartingPoint")
    private PlanStartingPointDTO planStartingPointDTO;

    // Starting point outcomes for breeding plans
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("breedingStartingPoints")
    private List<PlanStartingPointDTO> planStartingPointDTOS;

    // Starting point outcome for Es Cell Allele Modification.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("esCellAlleleModificationStartingPoint")
    private PlanStartingPointDTO modificationPlanStartingPointDTO;

    // Starting point outcome for Es Cell Allele Modification.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("crisprAlleleModificationStartingPoint")
    private PlanStartingPointDTO crisprModificationPlanStartingPointDTO;

    // Crispr attempt information. It will only contain information if the attempt type is crispr.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("crisprAttempt")
    private CrisprAttemptDTO crisprAttemptDTO;

    // Es cell attempt information. It will only contain information if the attempt type is es_cell.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("esCellAttempt")
    private EsCellAttemptDTO esCellAttemptDTO;

    // Breeding attempt information. It will only contain information if the attempt type is breeding.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("breedingAttempt")
    private BreedingAttemptDTO breedingAttemptDTO;

    // Es Cell Allele Modification attempt information. It will only contain information if the attempt type is 'es cell allele modification'.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("esCellAlleleModificationAttempt")
    private EsCellAlleleModificationAttemptDTO esCellAlleleModificationAttemptDTO;

    // Es Cell Allele Modification attempt information. It will only contain information if the attempt type is 'es cell allele modification'.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("crisprAlleleModificationAttempt")
    private CrisprAlleleModificationAttemptDTO crisprAlleleModificationAttemptDTO;

    // Phenotyping attempt information for creation. It will only contain information if the plan type is phenotyping.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("phenotypingAttempt")
    private PhenotypingAttemptCreationDTO phenotypingAttemptCreationDTO;

    // Phenotyping attempt information for response. It will only contain information if the plan type is phenotyping.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("phenotypingAttemptResponse")
    private PhenotypingAttemptResponseDTO phenotypingAttemptResponseDTO;

}
