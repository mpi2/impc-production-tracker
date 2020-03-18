package org.gentar.biology.plan.production.breeding_attempt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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
    private String deleterStrainName;
}
