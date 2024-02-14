package org.gentar.biology.mutation.symbolConstructor;

import static org.gentar.biology.mutation.constant.MutationErrors.*;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.util.Strings;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.mutation.MutationRepository;
import org.gentar.biology.mutation.categorizarion.MutationCategorization;
import org.gentar.biology.plan.attempt.AttemptTypesName;

public class AlleleSymbolConstructorImpl implements AlleleSymbolConstructor {
    private final MutationRepository mutationRepository;
    private static final String GENE_SYMBOL_SECTION = "[GENE_SYMBOL]";
    private static final String ID_SECTION = "[ID]";
    private static final String CONSORTIUM_ABBREVIATION_SECTION = "[CONSORTIUM_ABBREVIATION]";
    private static final String ILAR_CODE_SECTION = "[ILARCODE]";
    private final AttemptTypesName attemptTypesName;

    public AlleleSymbolConstructorImpl(MutationRepository mutationRepository,
                                       AttemptTypesName attemptTypesName) {
        this.mutationRepository = mutationRepository;
        this.attemptTypesName = attemptTypesName;
    }

    @Override
    public String calculateAlleleSymbol(
        SymbolSuggestionRequest symbolSuggestionRequest, Mutation mutation) {
        String result;
        String geneSymbol = getGeneSymbolByMutation(mutation);
        if (geneSymbol == null) {
            return ERROR_PLEASE_ENTER_GENE;
        }
        if ((attemptTypesName.equals(AttemptTypesName.ES_CELL)
            || attemptTypesName.equals(AttemptTypesName.ES_CELL_ALLELE_MODIFICATION))
            && getEscAlleleClass(mutation).isEmpty()) {
            return ERROR_PLEASE_SELECT_MUTATION_TYPE;
        }
        if (isEsCellAttemptTypeWrong(mutation)) {
            return THE_INITIAL_ES_CELL_PRODUCTION_ATTEMPT_A_E;
        } else if (isEsCellModificationAttemptTypeWrong(mutation)) {
            return TYPES_FOR_ES_CELL_MODIFICATION_ATTEMPT_B_C_D_E_1_1_2;
        }

        int nextId = getNextId(geneSymbol, symbolSuggestionRequest.getIlarCode());
        result = generateTemplate(getMutationType()).replace(GENE_SYMBOL_SECTION, geneSymbol);
        result = result.replace(ID_SECTION,
            nextId + getEscAlleleClassName(mutation));
        if (Strings.isBlank(symbolSuggestionRequest.getConsortiumAbbreviation())) {
            result = result.replace(CONSORTIUM_ABBREVIATION_SECTION, "");
        } else {
            String consortiumAbbreviation =
                "(" + symbolSuggestionRequest.getConsortiumAbbreviation() + ")";
            result = result.replace(CONSORTIUM_ABBREVIATION_SECTION, consortiumAbbreviation);
        }

        result = result.replace(ILAR_CODE_SECTION,
            symbolSuggestionRequest.getIlarCode() != null ? symbolSuggestionRequest.getIlarCode() :
                "");

        return result;
    }

    private List<String> getEscAlleleClass(Mutation mutation) {
        return mutation.getMutationCategorizations().stream()
            .filter(m -> m.getMutationCategorizationType().getName().equals("esc_allele_class"))
            .map(MutationCategorization::getName).collect(
                Collectors.toList());
    }

    private String getEscAlleleClassName(Mutation mutation) {
        return !getEscAlleleClass(mutation).isEmpty() ? getEscAlleleClass(mutation).getFirst() :
            "";
    }

    private int getNextId(String geneSymbol, String ilarCode) {


        String searchTerm = geneSymbol + "<" + getMutationType() + "%" + ilarCode + ">";
        List<Mutation> mutations = mutationRepository.findAllBySymbolLike(searchTerm);

        int nextId = mutations.size() + 1;

        for (Mutation mutation : mutations) {
            if (nextId <= findSymbolNextId(mutation.getSymbol())) {
                nextId = findSymbolNextId(mutation.getSymbol())+1;
            }
        }
        return nextId;
    }

    /**
     * Returns the gene symbol in the mutation. This assumes that only one gene exist for that mutation.
     *
     * @param mutation Mutation object.
     * @return Gene symbol
     */
    private String getGeneSymbolByMutation(Mutation mutation) {
        String geneSymbol = null;
        if (!mutation.getGenes().isEmpty()) {
            Gene gene = mutation.getGenes().iterator().next();
            geneSymbol = gene.getSymbol();
        }
        return geneSymbol;
    }

    private String generateTemplate(String mutationName) {
        return GENE_SYMBOL_SECTION +
            "<" +
            mutationName + ID_SECTION + CONSORTIUM_ABBREVIATION_SECTION + ILAR_CODE_SECTION +
            ">";
    }

    private String getMutationType() {
        if (attemptTypesName.equals(AttemptTypesName.CRISPR) ||
            attemptTypesName.equals(AttemptTypesName.CRISPR_ALLELE_MODIFICATION)) {
            return "em";
        } else if (attemptTypesName.equals(AttemptTypesName.ES_CELL) ||
            attemptTypesName.equals(AttemptTypesName.ES_CELL_ALLELE_MODIFICATION)) {
            return "tm";
        }
        return "";
    }

    private boolean isEsCellModificationAttemptTypeWrong(Mutation mutation) {
        return attemptTypesName.equals(AttemptTypesName.ES_CELL_ALLELE_MODIFICATION) &&
            (!getEscAlleleClassName(mutation).equals("b") &&
                !getEscAlleleClassName(mutation).equals("c") &&
                !getEscAlleleClassName(mutation).equals("d") &&
                !getEscAlleleClassName(mutation).equals("e.1") &&
                !getEscAlleleClassName(mutation).equals(".1") &&
                !getEscAlleleClassName(mutation).equals(".2"));
    }

    private boolean isEsCellAttemptTypeWrong(Mutation mutation) {
        return attemptTypesName.equals(AttemptTypesName.ES_CELL) &&
            (!getEscAlleleClassName(mutation).equals("a") &&
                !getEscAlleleClassName(mutation).equals("e") &&
                !getEscAlleleClassName(mutation).equals("''"));
    }


    public static String extractTextAfterSymbol(String symbol) {
        int index = symbol.indexOf('<');

        if (index != -1 && index < symbol.length() - 1) {
            // Found the symbol, and it's not the last character in the string
            return symbol.substring(index + 1);
        } else {
            // Symbol not found or found at the end of the string
            return null;
        }
    }

    public static int findSymbolNextId(String symbol) {

        String superScript = extractTextAfterSymbol(symbol);
        String[] numbers = superScript.split("\\D+"); // Split by non-digit characters
        int maxNumber = Integer.MIN_VALUE;

        for (String number : numbers) {
            if (!number.isEmpty()) {
                int currentNumber = Integer.parseInt(number);
                maxNumber = Math.max(maxNumber, currentNumber);
            }
        }

        return maxNumber;
    }

}
