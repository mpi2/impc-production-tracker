package org.gentar.biology.mutation.symbolCalculator;

import org.apache.logging.log4j.util.Strings;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.mutation.Mutation;

public class CrisprAlleleSymbolCalculator implements AlleleSymbolCalculator
{
    @Override
    public String calculateAlleleSymbol(Mutation mutation)
    {
        String result = mutation.getMgiAlleleSymbol();
        if (mutation.getMgiAlleleSymbolRequiresConstruction())
        {
            String geneSymbol = getGeneSymbolByMutation(mutation);
            if (!Strings.isBlank(result))
            {
                result = geneSymbol + "<" + result + ">";
            }
        }
        return result;
    }

    /**
     * Returns the gene symbol in the mutation. This assumes that only one gene exist for that mutation.
     * @param mutation Mutation object.
     * @return Gene symbol
     */
    private String getGeneSymbolByMutation(Mutation mutation)
    {
        String geneSymbol = null;
        if (!mutation.getGenes().isEmpty())
        {
            Gene gene = mutation.getGenes().iterator().next();
            geneSymbol = gene.getSymbol();
        }
        return geneSymbol;
    }
}
