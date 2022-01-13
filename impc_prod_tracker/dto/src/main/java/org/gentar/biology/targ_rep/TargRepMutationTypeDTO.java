package org.gentar.biology.targ_rep;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "targrep_mutation_types")
@Data
@RequiredArgsConstructor
public class TargRepMutationTypeDTO extends RepresentationModel<TargRepMutationTypeDTO> {
    private String name;
}
