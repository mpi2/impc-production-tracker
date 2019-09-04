package uk.ac.ebi.impc_prod_tracker.web.dto.plan.attempt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.attempt.phenotyping.PhenotypingAttemptDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.attempt.production.breeding_attempt.BreedingAttemptDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.attempt.production.crispr_attempt.CrisprAttemptDTO;

@Data
@RequiredArgsConstructor
public class AttemptDTO
{
    @JsonProperty("type_name")
    private String typeName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("crispr_attempt_attributes")
    private CrisprAttemptDTO crisprAttemptDTO;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("breeding_attempt_attributes")
    private BreedingAttemptDTO breedingAttemptDTO;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("phenotyping_attempt_attributes")
    private PhenotypingAttemptDTO phenotypingAttemptDTO;
}
