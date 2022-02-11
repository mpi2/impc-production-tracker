package org.gentar.biology.targ_rep;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "targrep_targeting_vectors")
@Data
@RequiredArgsConstructor
public class TargRepTargetingVectorResponseDTO {

    private Long id;

    private String name;
    private String intermediateVector;
    private Boolean reportToPublic;

    private TargRepAlleleResponseDTO allele;
    private TargRepPipelineResponseDTO pipeline;

    @JsonProperty("ikmc_project")
    private TargRepIkmcProjectDTO ikmcProject;

//    pipeline_id	Int
//    allele_id	Int
//    name	String
//    ikmc_project_id	String
//    intermediate_vector	String
//    report_to_public	Boolean

}
