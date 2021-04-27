package org.gentar.biology.plan.attempt.es_cell;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class EsCellAttemptDTO
{
    @JsonIgnore
    private Long esCellAttemptId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("imitsMiAttemptId")
    private Long imitsMiAttempt;

    private LocalDate miDate;

    @JsonProperty("attemptExternalRef")
    private String miExternalRef;

    private Boolean experimental;

    private String comment;


}
