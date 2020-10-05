package org.gentar.biology.gene_list;

import lombok.Data;
import java.util.List;

@Data
public class OutcomeSummaryDTO
{
    private String tpo;
    private List<String> molecularMutationTypeNames;
}
