package org.gentar.biology.outcome;

import lombok.Data;

/**
 * A DTO with some basic information about an outcome. (Used for example to create a list of
 * tpos (with additional information) associated to a project that can be used as starting points.
 */
@Data
public class OutcomeSummaryDTO
{
    private String tpo;
    private String externalReference;
}
