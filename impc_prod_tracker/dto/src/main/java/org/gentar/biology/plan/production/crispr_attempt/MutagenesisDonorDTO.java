package org.gentar.biology.plan.production.crispr_attempt;

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

    @JsonProperty("preparation")
    private String preparationTypeName;

    @JsonProperty("oligoSequenceFa")
    private String oligoSequenceFasta;
}
