package org.gentar.biology.targ_rep;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "targrep_alleles")
@Data
@RequiredArgsConstructor
public class TargRepAlleleResponseDTO extends RepresentationModel<TargRepAlleleResponseDTO> {

    private Long id;
    private String assembly;
    private String chromosome;
    private String strand;


    @JsonProperty("project_design_id")
    private Integer projectDesignId;

    @JsonProperty("design_type")
    private TargRepMutationTypeDTO mutationType;

    @JsonProperty("design_subtype")
    private TargRepMutationSubtypeDTO mutationSubtype;

    @JsonProperty("subtype_description")
    private String subtypeDescription;

    @JsonProperty("homology_arm_start")
    private Integer homologyArmStart;

    @JsonProperty("homology_arm_end")
    private Integer homologyArmEnd;

    @JsonProperty("cassette_start")
    private Integer cassetteStart;

    @JsonProperty("cassette_end")
    private Integer cassetteEnd;

    @JsonProperty("loxp_start")
    private Integer loxpStart;

    @JsonProperty("loxp_end")
    private Integer loxpEnd;

    @JsonProperty("mgi_accession_id")
    private List<String> mgiAccessionId;

    private String cassette;

    @JsonProperty("cassette_type")
    private String cassetteType;

    private String backbone;

    @JsonProperty("floxed_start_exon")
    private String floxedStartExon;

    @JsonProperty("floxed_end_exon")
    private String floxedEndExon;

}
