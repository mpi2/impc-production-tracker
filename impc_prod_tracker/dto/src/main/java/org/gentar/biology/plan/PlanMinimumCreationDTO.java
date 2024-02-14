package org.gentar.biology.plan;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

/**
 * This class has the fields that can be edited at any time (from PlanCommonDataDTO) plus data
 * that can only be set at the moment of creation.
 * Together, this results in the minimum information needed to create a plan. This is only used
 * when creating a project, which requires the creation of a small plan.
 */
@Data
public class PlanMinimumCreationDTO
{
    @JsonUnwrapped
    private PlanCommonDataDTO planCommonDataDTO;

    @JsonProperty("typeName")
    private String planTypeName;

    private String attemptTypeName;
}
