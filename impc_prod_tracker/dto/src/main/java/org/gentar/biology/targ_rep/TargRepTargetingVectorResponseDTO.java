package org.gentar.biology.targ_rep;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "targrep_targeting_vectors")
@Data
@RequiredArgsConstructor
public class TargRepTargetingVectorResponseDTO extends
    RepresentationModel<TargRepTargetingVectorResponseDTO> {

    private Long id;

    private String name;
    private String intermediateVector;
    private Boolean reportToPublic;
    private String ikmcProjectName;
    private TargRepAlleleResponseDTO allele;
    private TargRepPipelineResponseDTO pipeline;
    private String mgiAlleleNamePrediction;
    private Boolean productionCentreAutoUpdate;
    private String alleleTypePrediction;
    @JsonProperty("ikmc_project")
    private TargRepIkmcProjectDTO ikmcProject;


}
