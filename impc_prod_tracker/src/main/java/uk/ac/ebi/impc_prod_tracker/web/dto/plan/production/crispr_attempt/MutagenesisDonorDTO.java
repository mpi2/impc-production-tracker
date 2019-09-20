package uk.ac.ebi.impc_prod_tracker.web.dto.plan.production.crispr_attempt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MutagenesisDonorDTO
{
    private Long id;

    @JsonIgnore
    private Long attemptId;

    @JsonProperty("vector_name")
    private String vectorName;

    private Double concentration;

    @JsonProperty("preparation")
    private String preparationTypeName;

    @JsonProperty("oligo_sequence_fa")
    private String oligoSequenceFasta;
}
