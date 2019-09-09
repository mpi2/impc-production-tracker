package uk.ac.ebi.impc_prod_tracker.web.dto.plan.production.breeding_attempt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.web.dto.strain.StrainDTO;

@Data
@RequiredArgsConstructor
public class BreedingAttemptDTO
{
    @JsonIgnore
    private Long planId;
    @JsonIgnore
    private Long imitsMiMouseAlleleModId;
    private Integer numberOfCreMatingsStarted;
    private Integer numberOfCreMatingsSuccessful;
    private Boolean creExcesion;
    private Boolean tatCre;
    private StrainDTO deleterStrainAttributes;
}
