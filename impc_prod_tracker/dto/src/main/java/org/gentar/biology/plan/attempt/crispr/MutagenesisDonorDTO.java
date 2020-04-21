package org.gentar.biology.plan.attempt.crispr;

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
    @JsonIgnore
    private Long id;

    @JsonIgnore
    private Long attemptId;

    @JsonProperty("vectorName")
    private String vectorName;

    @JsonProperty("oligoSequenceFasta")
    private String oligoSequenceFasta;

    private Double concentration;

    @JsonProperty("preparationTypeName")
    private String preparationTypeName;
}
