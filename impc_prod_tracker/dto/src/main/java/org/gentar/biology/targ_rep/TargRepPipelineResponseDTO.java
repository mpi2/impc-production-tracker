package org.gentar.biology.targ_rep;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "pipelines")
@Data
@RequiredArgsConstructor
public class TargRepPipelineResponseDTO extends RepresentationModel<TargRepPipelineResponseDTO> {

    @JsonUnwrapped
    private TargRepPipelineDTO targRepPipelineDTO;


}
