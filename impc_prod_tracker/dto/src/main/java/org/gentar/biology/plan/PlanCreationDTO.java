package org.gentar.biology.plan;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

/**
 * Information that is set in the creation of a plan.
 */
@Data
public class PlanCreationDTO
{
    // Name of the plan type.
    private String planTypeName;

    // Name of the attempt type.
    private String attemptTypeName;

    // Common information for all plan dtos
    @JsonUnwrapped
    private PlanBasicDataDTO planBasicDataDTO;
}
