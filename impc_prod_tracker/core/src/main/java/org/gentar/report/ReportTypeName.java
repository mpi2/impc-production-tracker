package org.gentar.report;

import org.gentar.util.LabeledType;

import java.util.HashMap;
import java.util.Map;

/**
 * This enum should match the values of the name in report_type table.
 */
public enum ReportTypeName implements LabeledType {
    GENE_INTEREST("gene_interest"),
    MGI_CRISPR("mgi_crispr"),
    MGI_PHENOTYPING("mgi_phenotyping" );

    private static final Map<String, ReportTypeName> BY_LABEL = new HashMap<>();
    static
    {
        for (ReportTypeName e: values())
        {
            BY_LABEL.put(e.label, e);
        }
    }

    private final String label;

    ReportTypeName(String label)
    {
        this.label = label;
    }

    public static ReportTypeName valueOfLabel(String label)
    {
        return BY_LABEL.get(label);
    }

    @Override
    public String getLabel()
    {
        return label;
    }
}
