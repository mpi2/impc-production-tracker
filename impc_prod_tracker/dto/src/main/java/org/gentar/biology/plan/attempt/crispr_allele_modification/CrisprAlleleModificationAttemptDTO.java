package org.gentar.biology.plan.attempt.crispr_allele_modification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CrisprAlleleModificationAttemptDTO {
    @JsonIgnore
    private Long planId;

    private String modificationExternalRef;
    private Integer numberOfCreMatingsSuccessful;
    private Boolean tatCre;
    private String deleterStrainName;
}
