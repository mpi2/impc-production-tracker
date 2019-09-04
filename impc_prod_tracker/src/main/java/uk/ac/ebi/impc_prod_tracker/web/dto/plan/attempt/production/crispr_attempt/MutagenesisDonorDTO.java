package uk.ac.ebi.impc_prod_tracker.web.dto.plan.attempt.production.crispr_attempt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MutagenesisDonorDTO
{
    @JsonIgnore
    private Long id;
    @JsonIgnore
    private Long attemptId;

    @JsonProperty("vector_name")
    private String vectorName;

    private Double concentration;
    private String preparation;

    @JsonProperty("oligo_sequence_fa")
    private String oligoSequenceFa;
}
