package org.gentar.biology.plan.attempt.cre_allele_modification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CreAlleleModificationAttemptDTO {
    @JsonIgnore
    private Long planId;

    @JsonIgnore
    private Long imitsMouseAlleleMod;

    private Integer numberOfCreMatingsStarted;
    private Integer numberOfCreMatingsSuccessful;
    private Boolean creExcesion;
    private Boolean tatCre;
    private String deleterStrainName;
}
