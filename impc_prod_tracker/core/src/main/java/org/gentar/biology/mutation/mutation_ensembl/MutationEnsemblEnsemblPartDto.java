package org.gentar.biology.mutation.mutation_ensembl;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MutationEnsemblEnsemblPartDto {

    @JsonProperty("ensembl_gene_acc_id")
    private String ensemblGeneAccId;
    @JsonProperty("mgi_gene_acc_id")
    private String mgiGeneAccId;
    @JsonProperty("ensembl_chromosome")
    private String ensemblChromosome;
    @JsonProperty("ensembl_start")
    private Integer ensemblStart;
    @JsonProperty("ensembl_stop")
    private Integer ensemblStop;
    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("ensembl_strand")
    private String ensemblStrand;
}
