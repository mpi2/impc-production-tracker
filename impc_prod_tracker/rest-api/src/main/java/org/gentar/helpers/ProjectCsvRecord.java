package org.gentar.helpers;

import lombok.Data;

@Data
public class ProjectCsvRecord implements CsvRecord
{
    private static final String SEPARATOR = ",";
    public static final String[] HEADERS =
        new String[]
            {
                "Tpn",
                "External Reference",
                "Gene Or Location",
                "MGI",
                "Allele Intentions",
                "Best human Orthologs",
                "Project Assignment",
                "Privacy"
            };

    private String tpn;
    private String externalReference;
    private String geneSymbolOrLocation;
    private String accIds;
    private String alleleIntentions;
    private String bestHumanOrthologs;
    private String projectAssignment;
    private String privacy;

    public String toString()
    {
        return
            tpn + SEPARATOR +
            externalReference + SEPARATOR +
            geneSymbolOrLocation + SEPARATOR +
            accIds + SEPARATOR +
            alleleIntentions + SEPARATOR +
            bestHumanOrthologs + SEPARATOR +
            projectAssignment + SEPARATOR +
            privacy;
    }

    @Override
    public String[] getRowAsArray()
    {
        return new String[]
            {
                tpn,
                externalReference,
                geneSymbolOrLocation,
                accIds,
                alleleIntentions,
                bestHumanOrthologs,
                projectAssignment,
                privacy
            };
    }
}
