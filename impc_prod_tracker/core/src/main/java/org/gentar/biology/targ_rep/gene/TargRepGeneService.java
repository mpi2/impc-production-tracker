package org.gentar.biology.targ_rep.gene;

/**
 * TargRepGeneService.
 */
public interface TargRepGeneService {
    /**
     * Find a gene by it's symbol.
     *
     * @param symbol Accession id.
     * @return Gene object if something was found. Null otherwise.
     */
    TargRepGene getGeneBySymbolFailIfNull(String symbol);
}
