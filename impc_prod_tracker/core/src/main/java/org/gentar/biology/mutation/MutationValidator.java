package org.gentar.biology.mutation;

import static org.gentar.biology.mutation.constant.MutationErrors.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.mutation.categorizarion.MutationCategorization;
import org.gentar.biology.mutation.categorizarion.MutationCategorizationService;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.AttemptType;
import org.gentar.biology.plan.attempt.AttemptTypesName;
import org.gentar.exceptions.ForbiddenAccessException;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.security.permissions.Actions;
import org.gentar.security.permissions.Operations;
import org.springframework.stereotype.Component;


@Component
public class MutationValidator {

    private final ContextAwarePolicyEnforcement policyEnforcement;
    private final MutationRepository mutationRepository;
    private static final String CANNOT_READ_PLAN =
        "The mutation is linked to the plan %s and you " +
            "do not have permission to read it.";
    private static final String NULL_FIELD_ERROR = "%s cannot be null.";
    private static final List<String> newEnteredMutationSymbols = new ArrayList<>();

    public MutationValidator(ContextAwarePolicyEnforcement policyEnforcement,
                             MutationRepository mutationRepository,
                             MutationCategorizationService mutationCategorizationService) {
        this.policyEnforcement = policyEnforcement;
        this.mutationRepository = mutationRepository;
    }

    public void validateData(Mutation mutation) {
        Set<Gene> genes = mutation.getGenes();
        if (genes.isEmpty()) {
            throw new UserOperationFailedException(
                String.format(NULL_FIELD_ERROR, MUTATION_GENE_S));
        } else if (mutation.getMolecularMutationType() == null) {
            throw new UserOperationFailedException(
                String.format(NULL_FIELD_ERROR, MUTATION_MOLECULAR_MUTATION_TYPE_S));
        } else if (mutation.getSymbol() == null || mutation.getSymbol().equals("")) {
            throw new UserOperationFailedException(
                String.format(NULL_FIELD_ERROR, MUTATION_MUTATION_SYMBOL_S));
        } else if (!isSymbolInCorrectFormat(mutation)) {
            throw new UserOperationFailedException(
                MUTATION + mutation.getSymbol() +
                    SYMBOL_S_ARE_NOT_IN_THE_CORRECT_FORMAT);
        } else if (!isMutationSymbolUnique(mutation)) {
            throw new UserOperationFailedException(
                MUTATION + mutation.getSymbol() +
                    SYMBOL_S_ARE_NOT_UNIQUE);
        } else if (isEsCellAttemptTypeWrong(mutation)) {
            throw new UserOperationFailedException(
                THE_INITIAL_ES_CELL_PRODUCTION_ATTEMPT_A_E);

        } else if (isEsCellModificationAttemptTypeWrong(mutation)) {
            throw new UserOperationFailedException(
                TYPES_FOR_ES_CELL_MODIFICATION_ATTEMPT_B_C_D_E_1_1_2);
        }
    }

    private boolean isSymbolInCorrectFormat(Mutation mutation) {
        String symbol = mutation.getSymbol();
        if ((mutation.getGenes().size() != 1) ||
            mutation.getMolecularMutationType().getName().equals("Complex rearrangement")) {
            return false;
        }
        if (!symbol.contains("<") || !symbol.contains(">")) {
            return false;
        }

        if (!isWorkUnitIlarCodeValid(symbol,mutation)) {
            return false;
        }

        if (mutation.getGenes().stream()
            .noneMatch(g -> g.getSymbol().equals(symbol.split("<")[0]))) {
            return false;
        }

        if (isEsCellAttempt(mutation) && !symbol.split("<")[1].substring(0, 2).equals("tm")) {
            return false;
        }

        if (!isEsCellAttempt(mutation) && !symbol.split("<")[1].substring(0, 2).equals("em")) {
            return false;
        }
        if (!naturalNumbers().contains(symbol.split("<")[1].substring(2, 3))) {
            return false;
        }
        if (symbol.contains("(") && symbol.contains(")") &&
            symbol.substring(symbol.indexOf("(") + 1, symbol.indexOf(")")).equals("")) {
            return false;
        }
        if (getEscAlleleClassName(mutation).equals("''") &&
            !getSubStringMutationCategorization(symbol).equals("(") &&
            !Character.isUpperCase(getSubStringMutationCategorization(symbol).charAt(0))) {
            return false;
        }

        if (!getEscAlleleClassName(mutation).equals("") &&
            !getEscAlleleClassName(mutation).equals("''") && !getEscAlleleClassName(mutation)
            .contains(getSubStringMutationCategorization(symbol))) {
            return false;
        }

        return true;
    }

    private String getSubStringMutationCategorization(String symbol) {

        if (symbol.contains(".")) {
            if (symbol.charAt(symbol.indexOf(".") - 1) == 'e') {
                return symbol.substring(symbol.indexOf(".") - 1, symbol.indexOf(".") + 2);
            } else {
                return symbol.substring(symbol.indexOf("."), symbol.indexOf(".") + 2);
            }

        }

        return naturalNumbers().contains(symbol.split("<")[1].substring(3, 4)) ?
            symbol.split("<")[1].substring(4, 5) : symbol.split("<")[1].substring(3, 4);

    }

    private List<String> getEscAlleleClass(Mutation mutation) {
        return mutation.getMutationCategorizations().stream()
            .filter(m -> m.getMutationCategorizationType().getName().equals("esc_allele_class"))
            .map(MutationCategorization::getName).collect(
                Collectors.toList());
    }

    private String getEscAlleleClassName(Mutation mutation) {
        return getEscAlleleClass(mutation).size() != 0 ? getEscAlleleClass(mutation).get(0) :
            "";
    }

    private boolean isEsCellAttempt(Mutation mutation) {
        return mutation.getOutcomes().stream().map(m -> m.getPlan().getAttemptType().getName())
            .collect(
                Collectors.toList()).get(0).equals(AttemptTypesName.ES_CELL.getLabel()) ||
            mutation.getOutcomes().stream().map(m -> m.getPlan().getAttemptType().getName())
                .collect(
                    Collectors.toList()).get(0)
                .equals(AttemptTypesName.ES_CELL_ALLELE_MODIFICATION.getLabel());
    }

    private List<String> naturalNumbers() {
        return List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    }

    public void validateReadPermissions(Mutation mutation) {
        Set<Outcome> outcomes = mutation.getOutcomes();
        if (outcomes != null) {
            outcomes.forEach(outcome -> {
                Plan plan = outcome.getPlan();
                if (!policyEnforcement.hasPermission(plan, Actions.READ_PLAN_ACTION)) {
                    String detail = String.format(CANNOT_READ_PLAN, plan.getPin());
                    throwPermissionException(mutation, detail);
                }
            });
        }
    }

    private void throwPermissionException(Mutation mutation, String details) {
        String entityType = Mutation.class.getSimpleName();
        throw new ForbiddenAccessException(Operations.READ, entityType, mutation.getMin(), details);
    }

    private Boolean isMutationSymbolUnique(Mutation mutation) {
        if (mutation.getMin() != null) {
            newEnteredMutationSymbols.clear();
            return true;
        }
        if (mutationRepository.findAllBySymbolLike(mutation.getSymbol()).size() == 0 &&
            !newEnteredMutationSymbols
                .contains(mutation.getSymbol())) {
            newEnteredMutationSymbols.add(mutation.getSymbol());
            return true;
        }
        return false;
    }

    private String getAttemptTypeName(Mutation mutation) {
        return mutation.getOutcomes().stream().map(Outcome::getPlan).collect(Collectors.toList())
            .stream().map(Plan::getAttemptType).collect(Collectors.toList()).stream()
            .map(AttemptType::getName).collect(
                Collectors.toList()).get(0);
    }

    private boolean isWorkUnitIlarCodeValid(String symbol, Mutation mutation) {
        List<String> ilarCodes =
            mutation.getOutcomes().stream().map(Outcome::getPlan).collect(Collectors.toList())
                .stream().map(p -> p.getWorkUnit().getName()).collect(
                Collectors.toList());

        for (String ilarCode : ilarCodes) {
            if (symbol.toLowerCase().contains(ilarCode.toLowerCase())) {
                return true;
            }
        }
        return false;
    }


    private boolean isEsCellModificationAttemptTypeWrong(Mutation mutation) {
        return getAttemptTypeName(mutation)
            .equals(AttemptTypesName.ES_CELL_ALLELE_MODIFICATION.getLabel()) &&
            (!getEscAlleleClassName(mutation).equals("b") &&
                !getEscAlleleClassName(mutation).equals("c") &&
                !getEscAlleleClassName(mutation).equals("d") &&
                !getEscAlleleClassName(mutation).equals("e.1") &&
                !getEscAlleleClassName(mutation).equals(".1") &&
                !getEscAlleleClassName(mutation).equals(".2"));
    }

    private boolean isEsCellAttemptTypeWrong(Mutation mutation) {
        return getAttemptTypeName(mutation).equals(AttemptTypesName.ES_CELL.getLabel()) &&
            (!getEscAlleleClassName(mutation).equals("a") &&
                !getEscAlleleClassName(mutation).equals("e") &&
                !getEscAlleleClassName(mutation).equals("''"));
    }

}
