package org.gentar.biology.mutation.symbolConstructor;

import org.apache.logging.log4j.util.Strings;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.mutation.MutationRepository;
import java.util.List;

public class CrisprAlleleSymbolConstructor implements AlleleSymbolConstructor
{
    private final MutationRepository mutationRepository;
    private static final String GENE_SYMBOL_SECTION= "[GENE_SYMBOL]";
    private static final String ID_SECTION = "[ID]";
    private static final String ENDONUCLEASE_MUTATION_NAME = "em";
    private static final String CONSORTIUM_ABBREVIATION_SECTION= "[CONSORTIUM_ABBREVIATION]";
    private static final String ILAR_CODE_SECTION = "[ILARCODE]";

    private static final String template =
        GENE_SYMBOL_SECTION +
            "<" +
            ENDONUCLEASE_MUTATION_NAME + ID_SECTION + CONSORTIUM_ABBREVIATION_SECTION + ILAR_CODE_SECTION +
            ">";

    public CrisprAlleleSymbolConstructor(MutationRepository mutationRepository)
    {
        this.mutationRepository = mutationRepository;
    }

    @Override
    public String calculateAlleleSymbol(
        SymbolSuggestionRequest symbolSuggestionRequest, Mutation mutation)
    {
        String result;
        String geneSymbol = getGeneSymbolByMutation(mutation);
        int nextId = getNextId(geneSymbol, symbolSuggestionRequest.getIlarCode());
        result = template.replace(GENE_SYMBOL_SECTION, geneSymbol);
        result = result.replace(ID_SECTION, nextId + "");
        if (Strings.isBlank(symbolSuggestionRequest.getConsortiumAbbreviation()))
        {
            result = result.replace(CONSORTIUM_ABBREVIATION_SECTION, "");
        }
        else
        {
            String consortiumAbbreviation =
                "(" +  symbolSuggestionRequest.getConsortiumAbbreviation() + ")";
            result = result.replace(CONSORTIUM_ABBREVIATION_SECTION, consortiumAbbreviation);
        }
        result = result.replace(ILAR_CODE_SECTION, symbolSuggestionRequest.getIlarCode());
        return result;
    }

    private int getNextId(String geneSymbol, String ilarCode)
    {
        String searchTerm = geneSymbol + "<" + ENDONUCLEASE_MUTATION_NAME + "%" + ilarCode + ">";
        List<Mutation> mutations = mutationRepository.findAllBySymbolLike(searchTerm);
        return mutations.size() + 1;
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
