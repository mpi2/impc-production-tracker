package org.gentar.report;

import org.gentar.util.LabeledType;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * This enum should match the values of the name and description in report_type table.
 */
public enum ReportTypeName implements LabeledType {
    GENE_INTEREST("gene_interest",
            "This report is used by IMPC for register interest and feeds into the Solr gene core."),
    MGI_CRISPR("mgi_crispr",
            "This report is used by MGI to support their endonuclease-mediated allele load"),
    MGI_PHENOTYPING("mgi_phenotyping",
    "This report is used by MGI to support their HT mammalian phenotype load. It must be combined with ES Cell data stored in iMits.");

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

    ReportTypeName(String label, String description)
    {
        this.label = label;
        this.description = description;
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
}
