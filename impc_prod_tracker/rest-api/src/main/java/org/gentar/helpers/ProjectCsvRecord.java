package org.gentar.helpers;

import lombok.Data;

@Data
public class ProjectCsvRecord implements CsvRecord
{
    private static final String SEPARATOR = ",";
    public static final String[] HEADERS =
        new String[]
            {
                "TPN",
                "External Reference",
                "Gene Or Location",
                "MGI",
                "Mutation Intentions",
                "Best Human Orthologs",
                "Work Unit",
                "Work Group",
                "Project Assignment",
                "Project Summary Status",
                "Privacy",
                "Consortia"
            };

    private String tpn;
    private String externalReference;
    private String geneSymbolOrLocation;
    private String accIds;
    private String mutationIntentions;
    private String bestHumanOrthologs;
    private String workUnits;
    private String workGroups;
    private String projectAssignment;
    private String projectSummaryStatus;
    private String privacy;
    private String consortia;

    public String toString()
    {
        return
            tpn + SEPARATOR +
            externalReference + SEPARATOR +
            geneSymbolOrLocation + SEPARATOR +
            accIds + SEPARATOR +
                    mutationIntentions + SEPARATOR +
            bestHumanOrthologs + SEPARATOR +
            workUnits + SEPARATOR +
            workGroups + SEPARATOR +
            projectAssignment + SEPARATOR +
            projectSummaryStatus + SEPARATOR +
            privacy + SEPARATOR +
            consortia;
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
                mutationIntentions,
                bestHumanOrthologs,
                workUnits,
                workGroups,
                projectAssignment,
                projectSummaryStatus,
                privacy,
                consortia
            };
    }
}
