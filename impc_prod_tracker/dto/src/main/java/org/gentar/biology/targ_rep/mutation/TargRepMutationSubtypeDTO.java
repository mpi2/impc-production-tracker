package org.gentar.biology.targ_rep.mutation;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "targrep_mutation_subtypes")
@Data
@RequiredArgsConstructor
public class TargRepMutationSubtypeDTO extends RepresentationModel<TargRepMutationSubtypeDTO> {
    private String name;
}
