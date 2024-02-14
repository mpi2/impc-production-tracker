package org.gentar.report.utils.mutation_categorization;

import org.gentar.report.collection.mgi_crispr_allele.mutation_characterization.MgiCrisprAlleleReportMutationCategorizationProjection;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class MgiMutationCategorizationFormatHelperImpl implements MgiMutationCategorizationFormatHelper {

    @Override
    public String formatRepairMechanism(Set<MgiCrisprAlleleReportMutationCategorizationProjection> projections) {

        Predicate<String> isEmpty = String::isEmpty;
        Predicate<String> notEmpty = isEmpty.negate();

        String result = "";

        if (projections != null) {
            result = projections
                    .stream()
                    .map(this::formatRepairMechanismString)
                    .filter(notEmpty)
                    .collect(Collectors.joining("**"));

        }

        return result;
    }

    @Override
    public String formatAlleleCategory(Set<MgiCrisprAlleleReportMutationCategorizationProjection> projections) {

        Predicate<String> isEmpty = String::isEmpty;
        Predicate<String> notEmpty = isEmpty.negate();

        String result = "";

        if (projections != null) {
            result = projections
                    .stream()
                    .map(this::formatAlleleCategoryString)
                    .filter(notEmpty)
                    .collect(Collectors.joining("**"));

        }

        return result;
    }

    private String formatAlleleCategoryString(MgiCrisprAlleleReportMutationCategorizationProjection p) {
        if (p.getMutationCategorizationType() != null &&
                p.getMutationCategorizationType().contains(
                        MgiMutationCategorizationClassificationTypes.ALLELE_CATEGORY.getTypeName())) {
            return p.getMutationCategorization();
        } else {
            return "";
        }
    }

    private String formatRepairMechanismString(MgiCrisprAlleleReportMutationCategorizationProjection p) {
        if (p.getMutationCategorizationType() != null &&
                p.getMutationCategorizationType().contains(
                        MgiMutationCategorizationClassificationTypes.REPAIR_MECHANISM.getTypeName())) {
            return p.getMutationCategorization();
        } else {
            return "";
        }
    }
}
