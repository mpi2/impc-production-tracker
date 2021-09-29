package org.gentar.report.utils.mutation;

import org.gentar.util.LabeledType;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public enum EsCellMutationType implements LabeledType {

    // This specifies how the mutation_categorization in the database maps
    // to an allele classification when the mutation_categorization_type corresponds
    // to 'esc_allele_class' (i.e for ES Cell Alleles)

    // This is used to group mutations for the ES cell part of the Gene Interest Report.

    A("a","conditional"),
    B("b","null"),
    C("c","conditional"),
    D("d","null"),
    E("e","conditional"),
    PROMOTER_EXCISION_FROM_TM1E("e.1","conditional"),
    REPORTER_TAGGED_DELETION("''","null"),
    PROMOTER_EXCISION_TYPE_1(".1","null"),
    PROMOTER_EXCISION_TYPE_2(".2","null");

    private static final Map<String, EsCellMutationType> BY_LABEL = new HashMap<>();
    static
    {
        for (EsCellMutationType e: values())
        {
            BY_LABEL.put(e.label, e);
        }
    }

    private final String label;
    private final String classification;

    EsCellMutationType(String label, String classification)
    {
        this.label = label;
        this.classification = classification;

    }

    public static EsCellMutationType valueOfLabel(String label)
    {
        return BY_LABEL.get(label);
    }

    public static Stream<EsCellMutationType> stream() {
        return Stream.of(EsCellMutationType.values());
    }

    @Override
    public String getLabel()
    {
        return label;
    }

    public String getClassification() { return classification;}

}
