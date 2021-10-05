package org.gentar.biology.plan.attempt.es_cell_allele_modification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EsCellAlleleModificationAttemptDTO {
    @JsonIgnore
    private Long planId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("imitsMouseAlleleModId")
    private Long imitsMouseAlleleMod;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("imitsTargRepAlleleId")
    private Long targRepAllele;

    private String modificationExternalRef;
    private Integer numberOfCreMatingsSuccessful;
    private Boolean tatCre;
    private String deleterStrainName;
}
