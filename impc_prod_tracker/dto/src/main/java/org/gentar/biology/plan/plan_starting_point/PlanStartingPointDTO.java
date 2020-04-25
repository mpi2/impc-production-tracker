package org.gentar.biology.plan.plan_starting_point;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PlanStartingPointDTO
{
    @JsonIgnore
    private Long id;

    @JsonIgnore
    private Long planId;

    private String outcomeTypeName;
    private String colonyName;
    private String backgroundStrainName;
}
