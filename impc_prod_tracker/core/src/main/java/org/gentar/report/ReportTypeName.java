package org.gentar.report;

import org.gentar.util.LabeledType;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.FALSE;

/**
 * This enum should match the values of the name and description in report_type table.
 */
public enum ReportTypeName implements LabeledType {
    GENE_INTEREST("gene_interest",
            "This report is used by IMPC for register interest and feeds into the Solr gene core.",
            FALSE),
    MGI_CRISPR("mgi_crispr",
            "This report is used by MGI to support their endonuclease-mediated allele load",
            TRUE),
    MGI_INITIAL_CRISPR("mgi_initial_crispr",
        "This report is an initial version used by MGI to support their endonuclease-mediated allele load",
        TRUE),
    MGI_ES_CELL("mgi_es_cell",
            "This report is used by MGI to support their ES Cell allele load",
            TRUE),
    MGI_MODIFICATION("mgi_modification",
            "This report is used by MGI to support their ES Cell modification allele load",
            TRUE),
    MGI_PHENOTYPING("mgi_phenotyping",
            "This report is used by MGI to support their HT mammalian phenotype load. It must be combined with ES Cell data stored in iMits.",
            TRUE),
    PHENOTYPING_COLONIES("phenotyping_colonies",
                    "This report is used by IMPC to support the data release.",
                    FALSE),
    PRODUCTS("products",
            "This report is used by IMPC to support the data release.",
            FALSE),

    PRODUCTS_INTERMEDIATE_VECTORS("products_intermediate_vectors",
            "This report is used by IMPC to support the data release.",
            FALSE),

    PRODUCTS_TARGETING_VECTORS("products_targeting_vectors",
            "This report is used by IMPC to support the data release.",
            FALSE),

    PRODUCTS_ES_CELLS("products_es_cells",
            "This report is used by IMPC to support the data release.",
            FALSE),

    PRODUCTS_MICE("products_mice",
            "This report is used by IMPC to support the data release.",
            FALSE),

    PRODUCTS_CRISPR("products_crispr",
            "This report is used by IMPC to support the data release.",
            FALSE),

    PRODUCTS_ORDER("products_order",
            "This report is used by IMPC to support the data release.",
            FALSE),

    GENE_SYMBOL_UPDATE_LOG("gene_symbol_update_log",
        "This report is used record gene symbol updates in gentar.",
        FALSE),

    LANGUISHING_PROJECT_REPORT("languishing_project_report",
                                                  "This report is to identify projects that not changed status as genotyping confirmed after a certain time.",
                                          FALSE),
    MULTIPLE_ASSIGNED_PROJECT_REPORT("multiple_assigned_project_report",
                                   "This report is To identify genes which have multiple projects where more than one project is in the assigned state.",
                               FALSE),
    NO_ASSIGNED_PROJECT_REPORT("no_assigned_project_report",
                                   "This report is To identify genes where there is at least one project that is active, but none of the project have the assigned state.",
                               FALSE),
    ALL_INACTIVE_PROJECT_REPORT("all_inactive_project_report",
                                   "This report is to list all genes that has that all project are inactive status.",
                               FALSE),

    PRODUCTS_ALLELE("products_allele",
                           "This report is used by IMPC to support the data release.",
                   FALSE);

    private static final Map<String, ReportTypeName> BY_LABEL = new HashMap<>();
    static
    {
        for (ReportTypeName e: values())
        {
            BY_LABEL.put(e.label, e);
        }
    }

    private final String label;
    private final String description;
    private final Boolean isPublic;

    ReportTypeName(String label, String description, Boolean isPublic)
    {
        this.label = label;
        this.description = description;
        this.isPublic = isPublic;
    }

    public static ReportTypeName valueOfLabel(String label)
    {
        return BY_LABEL.get(label);
    }

    public static Stream<ReportTypeName> stream() {
        return Stream.of(ReportTypeName.values());
    }

    @Override
    public String getLabel()
    {
        return label;
    }

    public String getDescription() { return description;}

    public Boolean getIsPublic() { return isPublic;}
}

