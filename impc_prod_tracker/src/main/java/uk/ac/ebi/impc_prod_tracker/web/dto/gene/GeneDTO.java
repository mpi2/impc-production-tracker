package uk.ac.ebi.impc_prod_tracker.web.dto.gene;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@RequiredArgsConstructor
public class GeneDTO extends RepresentationModel
{
    @JsonIgnore
    private Long id;

    String mgiAccessionId;
    String markerSymbol;
    String specieName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String alleleTypeName;
}
