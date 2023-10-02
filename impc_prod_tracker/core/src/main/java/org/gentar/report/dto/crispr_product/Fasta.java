package org.gentar.report.dto.crispr_product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Fasta {
    private Integer index;
    private String sequence;
    private String sequenceType;
    private String sequenceCategory;
}
