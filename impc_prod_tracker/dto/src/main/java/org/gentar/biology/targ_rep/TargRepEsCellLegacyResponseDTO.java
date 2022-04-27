package org.gentar.biology.targ_rep;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "targrep_es_cells")
@Data
@RequiredArgsConstructor
public class TargRepEsCellLegacyResponseDTO
{
    private String name;
    private String pipelineName;
}
