package org.gentar.biology.plan.attempt.es_cell_allele_modification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EsCellAlleleModificationAttemptDTO {
    @JsonIgnore
    private Long planId;

    @JsonIgnore
    private Long imitsMouseAlleleMod;

    @JsonIgnore
    private Long targRepAllele;

    private String modificationExternalRef;

    private Integer numberOfCreMatingsSuccessful;
    private Boolean tatCre;
    private String deleterStrainName;
}
