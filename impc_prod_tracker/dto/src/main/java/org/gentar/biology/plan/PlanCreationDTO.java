package org.gentar.biology.plan;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

/**
 * Information that is set in the creation of a plan.
 */
@Data
public class PlanCreationDTO
{
    // Public identifier of the project for the plan.
    private String tpn;

    // Name of the plan type.
    @JsonProperty("typeName")
    private String planTypeName;

    // Name of the attempt type.
    private String attemptTypeName;

    // Common information for all plan dtos
    @JsonUnwrapped
    private PlanBasicDataDTO planBasicDataDTO;
}
