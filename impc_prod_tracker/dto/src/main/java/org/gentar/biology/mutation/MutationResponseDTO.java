package org.gentar.biology.mutation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.gentar.biology.gene.GeneResponseDTO;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Relation(collectionRelation = "mutations")
@Data
public class MutationResponseDTO extends RepresentationModel<MutationResponseDTO>
{
    private Long id;
    private String min;
    private String mgiAlleleId;
    private Boolean mgiAlleleSymbolWithoutImpcAbbreviation;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("imitsAlleleId")
    private Long imitsAllele;

    @JsonProperty("genes")
    private List<GeneResponseDTO> geneDTOS;

    @JsonUnwrapped
    private MutationCommonDTO mutationCommonDTO;
}
