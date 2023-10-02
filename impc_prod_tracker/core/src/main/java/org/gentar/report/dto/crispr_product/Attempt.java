package org.gentar.report.dto.crispr_product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Attempt {
    private String miDate;
    private String miExternalRef;
    private String mutagenesisExternalRef;
    private Integer totalEmbryosInjected;
    private Integer totalEmbryosSurvived;
    private Integer embryoTwoCell;
    private String comment;
    private String embryoTransferDay;
    private Integer totalTransferred;
    private String strainName;
    private String mgiStrainAccessionId;
}
