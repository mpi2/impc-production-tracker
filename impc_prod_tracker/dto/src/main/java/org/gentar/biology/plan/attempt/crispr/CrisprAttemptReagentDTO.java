package org.gentar.biology.plan.attempt.crispr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrisprAttemptReagentDTO
{
    private Long id;
    @JsonIgnore
    private Long crispr_attempt_plan_id;
    private String reagentName;
    private String reagentDescription;
    private Double concentration;
}
