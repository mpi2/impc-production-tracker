package uk.ac.ebi.impc_prod_tracker.data.biology.gene_list.gene_list_record;

import lombok.Data;

@Data
public class GeneByGeneListRecordDTO
{
    private Long id;
    private String accId;
    private String symbol;
    private String name;
}
