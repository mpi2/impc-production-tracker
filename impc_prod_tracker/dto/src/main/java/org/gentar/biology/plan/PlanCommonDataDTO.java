package org.gentar.biology.plan;

import lombok.Data;
import java.util.List;

/**
 * Common data shared by all plan dtos.
 */
@Data
public class PlanCommonDataDTO
{
    // List of names of the funders of the plan.
    private List<String> funderNames;

    // Name of the work unit of the plan.
    private String workUnitName;

    // Name of the work group of the plan.
    private String workGroupName;

    // Comment about the plan.
    private String comment;

    private Boolean productsAvailableForGeneralPublic;
}
