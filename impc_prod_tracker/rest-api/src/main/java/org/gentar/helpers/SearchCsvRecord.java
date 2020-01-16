package org.gentar.helpers;

import lombok.Data;

@Data
public class SearchCsvRecord implements CsvRecord
{
    private static final String SEPARATOR = ",";
    public static final String[] HEADERS =
        new String[]
            {
                "Input",
                "Search Comment",
                "Tpn",
                "Gene Or Location",
                "MGI",
                "Allele Intentions",
                "Best human Orthologs",
                "Work Unit",
                "Work Group",
                "Project Assignment",
                "Privacy",
                "Consortia"
            };

    private String input;
    private String searchComment;
    private String tpn;
    private String alleleIntentions;
    private String geneSymbolOrLocation;
    private String accIds;
    private String bestHumanOrthologs;
    private String workUnits;
    private String workGroups;
    private String projectAssignment;
    private String privacy;
    private String consortia;

    public String toString()
    {
        return
            input + SEPARATOR +
            searchComment + SEPARATOR +
            tpn + SEPARATOR +
            geneSymbolOrLocation + SEPARATOR +
            accIds + SEPARATOR +
            alleleIntentions + SEPARATOR +
            bestHumanOrthologs + SEPARATOR +
            workUnits + SEPARATOR +
            workGroups + SEPARATOR +
            projectAssignment + SEPARATOR +
            privacy + SEPARATOR +
            consortia;
    }

    @Override
    public String[] getRowAsArray()
    {
        return new String[]
            {
                input,
                searchComment,
                tpn,
                geneSymbolOrLocation,
                accIds,
                alleleIntentions,
                bestHumanOrthologs,
                workUnits,
                workGroups,
                projectAssignment,
                privacy,
                consortia
            };
    }
}
