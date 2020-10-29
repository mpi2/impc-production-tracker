package org.gentar.helpers;

import lombok.Data;

@Data
public class GeneListCsvRecord implements CsvRecord
{
    private static final String SEPARATOR = ",";
    public static final String[] HEADERS =
        new String[]
            {
                "Genes",
                "Note",
                "Types",
                "Visible",
                "Project"
            };

    private String genes;
    private String note;
    private String types;
    private Boolean visible;
    private String projectInformation;

    @Override
    public String toString()
    {
        return
            genes + SEPARATOR +
                note + SEPARATOR +
                types + SEPARATOR +
                visible + SEPARATOR +
                projectInformation;
    }

    @Override
    public String[] getRowAsArray()
    {
        return new String[]
            {
                genes, note, types, String.valueOf(visible), projectInformation
            };
    }
}
