package org.gentar.biology.targ_rep;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.core.Relation;


/**
 * TargRepEsCellDistributionCentreDTO.
 */
@Relation(collectionRelation = "targrep_es_cell_distribution_centre")
@Data
@RequiredArgsConstructor
public class TargRepEsCellDistributionCentreDTO {

    private Long id;
    private String name;
}
