package org.gentar.report.utils.mutation_categorization;

import lombok.Getter;

@Getter
public enum MgiMutationCategorizationClassificationTypes {
    REPAIR_MECHANISM("repair_mechanism"),
    ALLELE_CATEGORY("allele_category"),
    ES_CELL_ALLELE_CLASS("esc_allele_class");

    private final String typeName;


    MgiMutationCategorizationClassificationTypes(String typeName) {
        this.typeName = typeName;
    }

}
