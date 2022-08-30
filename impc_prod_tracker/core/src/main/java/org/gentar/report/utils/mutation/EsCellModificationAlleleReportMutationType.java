package org.gentar.report.utils.mutation;

import org.gentar.util.LabeledType;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public enum EsCellModificationAlleleReportMutationType implements LabeledType {

    // This specifies how the mutation_categorization in the database maps
    // to an allele classification when the mutation_categorization_type corresponds
    // to 'esc_allele_class' (i.e for ES Cell Alleles)

    // This is used to group mutations for the ES cell part of the Gene Interest Report.

    A("a", ""),
    B("b", "cre"),
    C("c", "flp"),
    D("d", "flp-cre"),
    E("e", ""),
    PROMOTER_EXCISION_FROM_TM1E("e.1", "cre"),
    REPORTER_TAGGED_DELETION("''", ""),
    PROMOTER_EXCISION_TYPE_1(".1", "cre"),
    PROMOTER_EXCISION_TYPE_2(".2", "flp");

    private static final Map<String, EsCellModificationAlleleReportMutationType> BY_LABEL = new HashMap<>();

    static {
        for (EsCellModificationAlleleReportMutationType e : values()) {
            BY_LABEL.put(e.label, e);
        }
    }

    private final String label;
    private final String classification;

    EsCellModificationAlleleReportMutationType(String label, String classification) {
        this.label = label;
        this.classification = classification;

    }

    public static EsCellModificationAlleleReportMutationType valueOfLabel(String label) {
        return BY_LABEL.get(label);
    }

    public static Stream<EsCellModificationAlleleReportMutationType> stream() {
        return Stream.of(EsCellModificationAlleleReportMutationType.values());
    }

    @Override
    public String getLabel() {
        return label;
    }

    public String getClassification() {
        return classification;
    }

}
