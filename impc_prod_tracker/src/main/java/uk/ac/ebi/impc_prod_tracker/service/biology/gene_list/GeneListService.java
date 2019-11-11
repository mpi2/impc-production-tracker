/******************************************************************************
 Copyright 2019 EMBL - European Bioinformatics Institute

 Licensed under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 either express or implied. See the License for the specific
 language governing permissions and limitations under the
 License.
 */
package uk.ac.ebi.impc_prod_tracker.service.biology.gene_list;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene_list.GeneList;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene_list.GeneListRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene_list.record.GeneListRecord;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene_list.record.GeneByGeneListRecord;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene_list.record.SortGeneByGeneListRecordByIndex;
import uk.ac.ebi.impc_prod_tracker.data.organization.consortium.Consortium;
import uk.ac.ebi.impc_prod_tracker.service.organization.consortium.ConsortiumService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class GeneListService
{
    private GeneListRepository geneListRepository;
    private GeneListRecordService geneListRecordService;
    private GeneListCsvConverter geneListCsvConverter;
    private ConsortiumService consortiumService;

    public GeneListService(
        GeneListRepository geneListRepository,
        GeneListRecordService geneListRecordService,
        GeneListCsvConverter geneListCsvConverter,
        ConsortiumService consortiumService)
    {
        this.geneListRepository = geneListRepository;
        this.geneListRecordService = geneListRecordService;
        this.geneListCsvConverter = geneListCsvConverter;
        this.consortiumService = consortiumService;
    }

    public GeneList getGeneListByConsortium(Consortium consortium)
    {
        return geneListRepository.findByConsortium(consortium);
    }

    public Page<GeneListRecord> getByConsortium(Pageable pageable, String consortiumName)
    {
        return geneListRecordService.getAllByConsortium(pageable, consortiumName);
    }

    public void updateRecordsInList(
        List<GeneListRecord> geneListRecords, String consortiumName)
    {
        Consortium consortium =
            consortiumService.getConsortiumByNameOrThrowException(consortiumName);
        final GeneList geneList = getOrCreateGeneList(consortium);

        validateNewRecords(new ArrayList<>(geneListRecords), geneList);

        geneListRecords.forEach(x -> {
            if (x.getGeneList() == null)
            {
                x.setGeneList(geneList);
            }
        });

        if (geneList.getGeneListRecords() == null)
        {
            geneList.setGeneListRecords(geneListRecords);
        }
        else
        {
            geneList.getGeneListRecords().addAll(geneListRecords);
        }
        geneListRepository.save(geneList);
    }

    private void validateNewRecords(List<GeneListRecord> listData, GeneList geneList)
    {
        if (geneList.getGeneListRecords() == null)
        {
            return;
        }
        Map<String, Long> sortedAccIdsInCurrentList = getAccIdHashesForGeneList(geneList);

        int size = listData.size();
        for (int i = 0; i < size; i++)
        {
            GeneListRecord geneListRecord = listData.get(i);
            List<String> symbols = new ArrayList<>();
            geneListRecord.getGenesByRecord().forEach(x -> symbols.add(x.getInputSymbolValue()));
            geneListRecordService.validateNewRecord(
                geneListRecord, sortedAccIdsInCurrentList, symbols.toString());
        }
    }

    /**
     * Updates the gene list for a consortium using the new data from csvContent, where each
     * element in the list is a record in a csv file.
     * @param consortiumName Consortium to be updated.
     * @param csvContent A list of strings representing the lines in a csv file.
     */
    public void updateListWithCsvContent(String consortiumName, List<List<String>> csvContent)
    {
        GeneList geneList = buildListFromCsvContent(consortiumName, csvContent);
        geneListRepository.save(geneList);
    }

    private GeneList buildListFromCsvContent(
        String consortiumName, List<List<String>> csvContent)
    {
        Consortium consortium =
            consortiumService.getConsortiumByNameOrThrowException(consortiumName);
        final GeneList geneList = getOrCreateGeneList(consortium);
        Map<String, Long> sortedAccIdsInCurrentList = getAccIdHashesForGeneList(geneList);
        Map<String, List<String>> recordsByColumns =
            geneListCsvConverter.processCsvContent(csvContent);
        var listData =
            geneListCsvConverter.buildListFromCsvContent(recordsByColumns, sortedAccIdsInCurrentList);
        validateNewCsvRecords(new ArrayList<>(listData), geneList, recordsByColumns);
        listData.forEach(x -> x.setGeneList(geneList));
        if (geneList.getGeneListRecords() == null)
        {
            geneList.setGeneListRecords(listData);
        }
        else
        {
            geneList.getGeneListRecords().addAll(listData);
        }
        return geneList;
    }

    private Map<String, Long> getAccIdHashesForGeneList(GeneList geneList)
    {
        Map<String, Long> sortedAccIdsInCurrentList = new HashMap<>();
        var geneListRecords = geneList.getGeneListRecords();
        if (geneListRecords != null)
        {
            geneListRecords.forEach(x ->
                sortedAccIdsInCurrentList.put(getAccIdHashByGeneListRecord(x), x.getId()));
        }

        return sortedAccIdsInCurrentList;
    }

    private void validateNewCsvRecords(
        List<GeneListRecord> listData,
        GeneList geneList, Map<String,
        List<String>> recordsByColumns)
    {
        if (geneList.getGeneListRecords() == null)
        {
            return;
        }
        Map<String, Long> sortedAccIdsInCurrentList = getAccIdHashesForGeneList(geneList);

        List<String> geneColumnContent =
            recordsByColumns.get(GeneListCsvConverter.CSV_GENE_HEADER);

        int size = listData.size();
        for (int i = 0; i < size; i++)
        {
            GeneListRecord geneListRecord = listData.get(i);
            String geneSymbols = geneColumnContent.get(i);
            geneListRecordService.validateNewRecord(
                geneListRecord, sortedAccIdsInCurrentList, geneSymbols);
        }
    }

    private String getAccIdHashByGeneListRecord(GeneListRecord geneListRecord)
    {
        var genes = new ArrayList<>(geneListRecord.getGenesByRecord());
        genes.sort(new SortGeneByGeneListRecordByIndex());
        return genesByRecordToString(genes);
    }

    public String genesByRecordToString(List<GeneByGeneListRecord> genes)
    {
        StringBuilder result = new StringBuilder();
        if (genes != null)
        {
            genes.forEach(x -> {
                result.append(x.getAccId()).append("-");
            } );
        }
        return result.toString();
    }

    private GeneList getOrCreateGeneList(Consortium consortium)
    {
        GeneList geneList = getGeneListByConsortium(consortium);
        if (geneList == null)
        {
            geneList = createGeneList(consortium);
        }
        return geneList;
    }

    private GeneList createGeneList(Consortium consortium)
    {
        GeneList geneList = new GeneList();
        geneList.setConsortium(consortium);
        return geneList;
    }
}
