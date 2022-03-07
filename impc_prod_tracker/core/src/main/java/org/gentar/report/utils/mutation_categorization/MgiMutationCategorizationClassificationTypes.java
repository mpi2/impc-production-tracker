package org.gentar.report.utils.mutation_categorization;

public enum MgiMutationCategorizationClassificationTypes {
    REPAIR_MECHANISM("repair_mechanism"),
    ALLELE_CATEGORY("allele_category"),
    ES_CELL_ALLELE_CLASS("esc_allele_class");

    private String typeName;


    MgiMutationCategorizationClassificationTypes(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName()
    {
        return typeName;
    }
}
