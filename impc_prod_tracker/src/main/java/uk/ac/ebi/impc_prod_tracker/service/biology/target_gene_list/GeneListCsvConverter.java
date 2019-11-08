package uk.ac.ebi.impc_prod_tracker.service.biology.target_gene_list;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.exceptions.UserOperationFailedException;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.Gene;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene_list.gene_list_record.GeneByGeneListRecord;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene_list.gene_list_record.GeneListRecord;
import uk.ac.ebi.impc_prod_tracker.service.biology.gene.external_ref.GeneExternalService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class GeneListCsvConverter
{
    public final static String CSV_GENE_HEADER = "Gene";
    public final static String CSV_NOTE_HEADER = "Note";

    private GeneExternalService geneExternalService;

    private final static String HEADER_NOT_FOUND_ERROR = "Header %s not found in csv file.";

    public GeneListCsvConverter(GeneExternalService geneExternalService)
    {
        this.geneExternalService = geneExternalService;
    }

    public Set<GeneListRecord> processCsvContent(List<List<String>> csvContent)
    {
        Map<String, List<String>> recordsByColumns = new HashMap<>();
        validateCsvHeaders(csvContent);
        List<String> genesColumnContent = getElementsInColumn(csvContent, CSV_GENE_HEADER);
        List<String> notesColumnContent = getElementsInColumn(csvContent, CSV_NOTE_HEADER);
        recordsByColumns.put(CSV_GENE_HEADER, genesColumnContent);
        recordsByColumns.put(CSV_NOTE_HEADER, notesColumnContent);
        validateSameRowCountForAllColumns(recordsByColumns);
        return buildListFromCsvContent(recordsByColumns);
    }

    private void validateSameRowCountForAllColumns(Map<String, List<String>> recordsByColumns)
    {
        List<Integer> sizes = new ArrayList<>();
        recordsByColumns.forEach((k, v) -> {
            sizes.add(v.size());
        });
        int firstElementSize = sizes.get(0);
        boolean sameSizeForAll = sizes.stream().allMatch(x -> x.equals(firstElementSize));
        if (!sameSizeForAll)
        {
            throw new UserOperationFailedException("Columns in csv have different number of records");
        }
    }

    private void validateCsvHeaders(List<List<String>> csvContent)
    {
        List<String> headers = getHeaders(csvContent);
        if (!headers.contains(CSV_GENE_HEADER))
        {
            throw new UserOperationFailedException(
                String.format(HEADER_NOT_FOUND_ERROR, CSV_GENE_HEADER));
        }
        if (!headers.contains(CSV_NOTE_HEADER))
        {
            throw new UserOperationFailedException(
                String.format(HEADER_NOT_FOUND_ERROR, CSV_NOTE_HEADER));
        }
    }

    private List<String> getHeaders(List<List<String>> csvContent)
    {
        return csvContent.get(0);
    }

    private List<String> getElementsInColumn(List<List<String>> csvContent, String csvGeneHeader)
    {
        List<List<String>> data = csvContent.subList(1, csvContent.size());
        List<String> headers = getHeaders(csvContent);
        int headerPosition = headers.indexOf(csvGeneHeader);
        List<String> elements = new ArrayList<>();
        data.forEach(line -> {
            elements.add(line.get(headerPosition));
        });
        return elements;
    }

    private Set<GeneListRecord> buildListFromCsvContent(
        Map<String, List<String>> recordsByColumns)
    {
        Set<GeneListRecord> listData = new HashSet<>();
        List<String> geneColumnContent = recordsByColumns.get(CSV_GENE_HEADER);
        List<String> noteColumnContent = recordsByColumns.get(CSV_NOTE_HEADER);
        int numberOfRows = geneColumnContent.size();
        for (int i = 0; i < numberOfRows; i++)
        {
            GeneListRecord geneListRecord =
                buildGeneListRecord(geneColumnContent.get(i), noteColumnContent.get(i));
            listData.add(geneListRecord);
        }
        return listData;
    }

    private GeneListRecord buildGeneListRecord(String geneColumnContent, String note)
    {
        GeneListRecord geneListRecord = new GeneListRecord();
        List<String> geneSymbols = Arrays.asList(geneColumnContent.split(","));
        Set<GeneByGeneListRecord> geneByGeneListRecords = buildGeneByGeneListRecords(geneSymbols);
        geneListRecord.setNote(note);
        geneListRecord.setGenesByRecord(geneByGeneListRecords);
        geneByGeneListRecords.forEach(x -> x.setGeneListRecord(geneListRecord));
        return geneListRecord;
    }

    private Set<GeneByGeneListRecord> buildGeneByGeneListRecords(List<String> geneSymbols)
    {
        Set<GeneByGeneListRecord> geneByGeneListRecords = new HashSet<>();
        List<Gene> genes = getGenesByListOfSymbols(geneSymbols);
        if (genes != null)
        {
            genes.forEach(x -> geneByGeneListRecords.add(buildGeneByGeneListRecord(x)));
        }
        return geneByGeneListRecords;
    }

    private GeneByGeneListRecord buildGeneByGeneListRecord(Gene gene)
    {
        GeneByGeneListRecord geneByGeneListRecord = new GeneByGeneListRecord();
        geneByGeneListRecord.setAccId(gene.getAccId());
        return geneByGeneListRecord;
    }

    private List<Gene> getGenesByListOfSymbols(List<String> geneSymbols)
    {
        List<Gene> genes = new ArrayList<>();
        geneSymbols.forEach(x -> {
            Gene gene = geneExternalService.getGeneFromExternalDataBySymbolOrAccId(x);
            if (gene == null)
            {
                throw new UserOperationFailedException(x + " is not a valid gene symbol.");
            }
            genes.add(gene);
        });
        return genes;
    }
}