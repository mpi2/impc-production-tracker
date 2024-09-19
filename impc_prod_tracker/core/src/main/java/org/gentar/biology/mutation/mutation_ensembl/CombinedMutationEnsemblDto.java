package org.gentar.biology.mutation.mutation_ensembl;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CombinedMutationEnsemblDto {

    private String min;
    private String ensemblGeneAccId;
    private String mgiGeneAccId;
    private String ensemblChromosome;
    private Integer ensemblStart;
    private Integer ensemblStop;
    private String symbol;
    private String ensemblStrand;
}
