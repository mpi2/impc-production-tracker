package org.gentar.helpers;

import lombok.Data;

@Data
public class ProjectCsvRecord implements CsvRecord
{
    private static final String SEPARATOR = ",";
    public static final String[] HEADERS =
        new String[]
            {
                "Input",
                "Search Comment",
                "Tpn",
                "Allele Intentions",
                "Gene Or Location",
                "Best human Orthologs",
                "Project Assignment",
                "Privacy"
            };

    private String input;
    private String searchComment;
    private String tpn;
    private String alleleIntentions;
    private String geneSymbolOrLocation;
    private String bestHumanOrthologs;
    private String projectAssignment;
    private String privacy;

    public String toString()
    {
        return input + SEPARATOR +
            searchComment + SEPARATOR +
            tpn + SEPARATOR;
    }

    @Override
    public String[] getRowAsArray()
    {
        return new String[]
            {
                input,
                searchComment,
                tpn,
                alleleIntentions,
                geneSymbolOrLocation,
                bestHumanOrthologs,
                projectAssignment,
                privacy
            };
    }
}
