package org.gentar.biology.targ_rep.pipeline;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "targrep_pipelines")
@Data
@RequiredArgsConstructor
public class TargRepPipelineResponseDTO extends RepresentationModel<TargRepPipelineResponseDTO> {

    private Long id;

    private String name;

    private String description;

    @JsonProperty("geneTrap")
    private Boolean geneTrap;

    @JsonProperty("reportToPublic")
    private Boolean reportToPublic;


}
