package uk.ac.ebi.impc_prod_tracker.web.dto.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import uk.ac.ebi.impc_prod_tracker.web.dto.strain.StrainDTO;

@Data
@RequiredArgsConstructor
public class LocationDTO extends RepresentationModel
{
    @JsonIgnore
    private Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String alleleTypeName;

    Integer chr;
    Integer start;
    Integer stop;
    String strand;
    String genomeBuild;
    StrainDTO strainAttributes;
    String specieName;
    String sequenceType;
    Long chrFeatureTypeId;
}
