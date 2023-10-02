package org.gentar.report.dto.crispr_product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MutagenesisDonor
{

    private Long id;
    private Long attemptId;
    private String vector_name;
    private String oligoSequenceFasta;
    private String sequence;
    private String name;
    private Double concentration;
    private String preparationTypeName;
}
