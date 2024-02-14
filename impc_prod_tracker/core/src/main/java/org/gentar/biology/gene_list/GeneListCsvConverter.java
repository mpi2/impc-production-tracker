package org.gentar.biology.gene_list;

import org.apache.logging.log4j.util.Strings;
import org.gentar.biology.gene.external_ref.GeneExternalService;
import org.gentar.biology.gene_list.record.GeneByListRecord;
import org.gentar.biology.gene_list.record.ListRecord;
import org.gentar.biology.gene_list.record.ListRecordType;
import org.gentar.biology.gene_list.record.ListRecordTypeService;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class GeneListCsvConverter
{
    public final static String CSV_GENE_HEADER = "Genes";
    public final static String CSV_NOTE_HEADER = "Note";
    public final static String CSV_TYPE_HEADER = "Type";
    public final static String CSV_VISIBLE_HEADER = "Visible";

    private final GeneExternalService geneExternalService;
    private final GeneListRecordService geneListRecordService;
    private final ListRecordTypeService listRecordTypeService;

    private final static String HEADER_NOT_FOUND_ERROR = "Header %s not found in csv file.";

    public GeneListCsvConverter(
        GeneExternalService geneExternalService,
        GeneListRecordService geneListRecordService,
        ListRecordTypeService listRecordTypeService)
    {
        this.geneExternalService = geneExternalService;
        this.geneListRecordService = geneListRecordService;
        this.listRecordTypeService = listRecordTypeService;
    }

    public Map<String, List<String>> processCsvContent(
        List<List<String>> csvContent)
    {
        Map<String, List<String>> recordsByColumns = new HashMap<>();
        validateCsvHeaders(csvContent);
        List<String> genesColumnContent = getElementsInColumn(csvContent, CSV_GENE_HEADER);
        List<String> notesColumnContent = getElementsInColumn(csvContent, CSV_NOTE_HEADER);
        List<String> typesColumnContent = getElementsInColumn(csvContent, CSV_TYPE_HEADER);
        List<String> visiblesColumnContent = getElementsInColumn(csvContent, CSV_VISIBLE_HEADER);
        recordsByColumns.put(CSV_GENE_HEADER, genesColumnContent);
        recordsByColumns.put(CSV_NOTE_HEADER, notesColumnContent);
        recordsByColumns.put(CSV_TYPE_HEADER, typesColumnContent);
        recordsByColumns.put(CSV_VISIBLE_HEADER, visiblesColumnContent);
        validateSameRowCountForAllColumns(recordsByColumns);
        return recordsByColumns;
    }

    private void validateSameRowCountForAllColumns(Map<String, List<String>> recordsByColumns)
    {
        List<Integer> sizes = new ArrayList<>();
        recordsByColumns.forEach((k, v) -> sizes.add(v.size()));
        int firstElementSize = sizes.getFirst();
        boolean sameSizeForAll = sizes.stream().allMatch(x -> x.equals(firstElementSize));
        if (!sameSizeForAll)
        {
            throw new UserOperationFailedException(
                "Columns in csv have different number of records");
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
        if (!headers.contains(CSV_TYPE_HEADER))
        {
            throw new UserOperationFailedException(
                String.format(HEADER_NOT_FOUND_ERROR, CSV_TYPE_HEADER));
        }
    }

    private List<String> getHeaders(List<List<String>> csvContent)
    {
        List<String> headers = csvContent.getFirst();
        List<String> cleanedHeaders = new ArrayList<>();
        for (String header : headers)
        {
            header = header.replace('\u00A0',' ');
            header = header.replace('\u2007',' ');
            header = header.replace('\u202F',' ');
            header = header.replace('\uFEFF',' ');
            header = header.trim();
            cleanedHeaders.add(header);
        }
        return cleanedHeaders;
    }

    private List<String> getElementsInColumn(List<List<String>> csvContent, String csvGeneHeader)
    {
        List<List<String>> data = csvContent.subList(1, csvContent.size());
        List<String> headers = getHeaders(csvContent);
        int headerPosition = headers.indexOf(csvGeneHeader);
        List<String> elements = new ArrayList<>();
        data.forEach(line -> elements.add(line.get(headerPosition)));
        return elements;
    }

    public List<ListRecord> buildListFromCsvContent(
        String consortiumName,
        Map<String, List<String>> recordsByColumns,
        Map<String, Long> sortedAccIdsInCurrentList)
    {
        List<ListRecord> listData = new ArrayList<>();
        List<String> geneColumnContent = recordsByColumns.get(CSV_GENE_HEADER);
        List<String> noteColumnContent = recordsByColumns.get(CSV_NOTE_HEADER);
        List<String> typesColumnContent = recordsByColumns.get(CSV_TYPE_HEADER);
        List<String> visiblesColumnContent = recordsByColumns.get(CSV_VISIBLE_HEADER);

        // Get all the accIds for the symbols so we can avoid individual calls to retrieve the information
        List<String> symbols = getSymbolsList(geneColumnContent);
        Map<String, String> accIdsBySymbolsMap = geneExternalService.getAccIdsBySymbolsBulk(symbols);

        int numberOfRows = geneColumnContent.size();
        for (int i = 0; i < numberOfRows; i++)
        {
            ListRecord listRecord =
                buildGeneListRecord(
                    consortiumName,
                    geneColumnContent.get(i),
                    noteColumnContent.get(i),
                    typesColumnContent.get(i),
                    visiblesColumnContent.get(i),
                    sortedAccIdsInCurrentList,
                    accIdsBySymbolsMap);
            listData.add(listRecord);
        }
        return listData;
    }

    private List<String> getSymbolsList(List<String> geneColumnContent)
    {
        List<String> symbols = new ArrayList<>();
        geneColumnContent.forEach(x -> {
            List<String> symbolsInRecord = Arrays.asList(x.split(","));
            symbols.addAll(symbolsInRecord);
        });
        return symbols;
    }

    private ListRecord buildGeneListRecord(
        String consortiumName,
        String geneColumnContent,
        String note,
        String typeColumnContent,
        String visibleColumnContent,
        Map<String, Long> sortedAccIdsInCurrentList, Map<String,
        String> accIdsBySymbolsMap)
    {
        ListRecord listRecord = new ListRecord();
        List<String> geneSymbols = Arrays.asList(geneColumnContent.split(","));
        List<String> types = Arrays.asList(typeColumnContent.split(","));
        Set<GeneByListRecord> geneByListRecords =
            buildGeneByGeneListRecords(geneSymbols, sortedAccIdsInCurrentList, accIdsBySymbolsMap);
        listRecord.setNote(note);
        listRecord.setVisible(Boolean.parseBoolean(visibleColumnContent.toLowerCase()));
        listRecord.setGenesByRecord(geneByListRecords);
        listRecord.setListRecordTypes(getRecordTypesByNames(consortiumName, types));
        geneByListRecords.forEach(x -> x.setListRecord(listRecord));
        return listRecord;
    }

    private Set<ListRecordType> getRecordTypesByNames(String consortiumName, List<String> recordTypesNames)
    {
        Set<ListRecordType> listRecordTypes = new HashSet<>();
        recordTypesNames.forEach(x -> {
            if (!Strings.isBlank(x))
            {
                ListRecordType listRecordType =
                    listRecordTypeService.getRecordTypeByTypeNameAndConsortiumName(x, consortiumName);
                if (listRecordType == null)
                {
                    throw new UserOperationFailedException(
                        "Record type " + x + " does not exist in consortium " + consortiumName + ".");
                }
                listRecordTypes.add(listRecordType);
            }
        });
        return listRecordTypes;
    }

    private Set<GeneByListRecord> buildGeneByGeneListRecords(
        List<String> geneSymbols, Map<String, Long> currentListSymbols, Map<String, String> accIdsBySymbolsMap)
    {
        Set<GeneByListRecord> geneByListRecords = new HashSet<>();
        List<String> accIds = getAccIdsByListOfSymbols(geneSymbols, accIdsBySymbolsMap);
        AtomicInteger index = new AtomicInteger();
        accIds.forEach(x -> {
            var geneByGeneListRecord = buildGeneByGeneListRecord(x);
            geneByGeneListRecord.setIndex(index.getAndIncrement());
            geneByListRecords.add(geneByGeneListRecord);
        });
        String inputAccIdHash =
            geneListRecordService.genesByRecordToString(geneByListRecords);
        if (currentListSymbols.containsKey(inputAccIdHash))
        {
            throw new UserOperationFailedException(geneSymbols + " already exist in the list.");
        }

        return geneByListRecords;
    }

    private GeneByListRecord buildGeneByGeneListRecord(String accId)
    {
        GeneByListRecord geneByListRecord = new GeneByListRecord();
        geneByListRecord.setAccId(accId);
        return geneByListRecord;
    }

    private List<String> getAccIdsByListOfSymbols(List<String> geneSymbols, Map<String, String> accIdsBySymbolsMap)
    {
        List<String> accIds = new ArrayList<>();
        geneSymbols.forEach(x -> {
            String accId = accIdsBySymbolsMap.getOrDefault(x.toLowerCase(), null);
            if (accId == null)
            {
                throw new UserOperationFailedException(x + " is not a valid gene symbol.");
            }
            accIds.add(accId);
        });
        return accIds;
    }
}
