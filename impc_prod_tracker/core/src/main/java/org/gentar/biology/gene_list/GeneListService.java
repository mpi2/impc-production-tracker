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
package org.gentar.biology.gene_list;

import org.gentar.biology.gene_list.filter.GeneListFilter;
import org.gentar.biology.gene_list.record.*;
import org.gentar.organization.consortium.ConsortiumService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.gentar.organization.consortium.Consortium;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class GeneListService
{
    private final GeneListRepository geneListRepository;
    private final GeneListRecordService geneListRecordService;
    private final GeneListCsvConverter geneListCsvConverter;
    private final ConsortiumService consortiumService;

    public GeneListService(GeneListRepository geneListRepository,
        GeneListRecordService geneListRecordService,
        GeneListCsvConverter geneListCsvConverter,
        ConsortiumService consortiumService)
    {
        this.geneListRepository = geneListRepository;
        this.geneListRecordService = geneListRecordService;
        this.geneListCsvConverter = geneListCsvConverter;
        this.consortiumService = consortiumService;
    }

    public List<GeneList> getAll()
    {
        return geneListRepository.findAll();
    }

    public GeneList getGeneListByConsortium(Consortium consortium)
    {
        return geneListRepository.findByConsortium(consortium);
    }

    public Page<ListRecord> getAllWithFilters(
        Pageable pageable, GeneListFilter filter)
    {
        return geneListRecordService.getAllBySpecs(pageable, filter);
    }

    public List<ListRecord> getAllNotPaginatedWithFilters(GeneListFilter filter)
    {
        return geneListRecordService.getAllNotPaginated(filter);
    }

    public Stream<ListRecord> getAllStream(GeneListFilter filter)
    {
        return geneListRecordService.getAllStream(filter);
    }

    public List<String> getAllAccIdsByConsortiumId(Long consortiumId)
    {
        return geneListRecordService.getAllAccIdsByConsortiumId(consortiumId);
    }

    public void updateRecordsInList(
        List<ListRecord> listRecords, String consortiumName)
    {
        Consortium consortium =
            consortiumService.getConsortiumByNameOrThrowException(consortiumName);
        final GeneList geneList = getOrCreateGeneList(consortium);

        validateNewRecords(listRecords, geneList);

        listRecords.forEach(x -> {
            if (x.getGeneList() == null)
            {
                x.setGeneList(geneList);
            }
        });

        if (geneList.getListRecords() == null)
        {
            geneList.setListRecords(listRecords);
        }
        else
        {
            addOrReplaceListRecords(listRecords, geneList);
        }
        geneListRepository.save(geneList);
    }

    public void deleteRecordsInList(
        List<Long> listRecordsIds, String consortiumName)
    {
        Consortium consortium =
            consortiumService.getConsortiumByNameOrThrowException(consortiumName);
        final GeneList geneList = getOrCreateGeneList(consortium);

        if (geneList.getListRecords() != null)
        {
            removeListRecords(listRecordsIds, geneList);
        }
        geneListRepository.save(geneList);
    }

    private void addOrReplaceListRecords(List<ListRecord> listRecords, GeneList geneList)
    {
        var currentListRecords = geneList.getListRecords();
        listRecords.forEach(x -> {
            var index = indexOfRecordIdInList(x.getId(), currentListRecords);
            if (index == -1)
            {
                currentListRecords.add(x);
            }
            else
            {
                currentListRecords.set(index, x);
            }
        });
    }

    private void removeListRecords(List<Long> listRecordsIds, GeneList geneList)
    {
        var currentListRecords = geneList.getListRecords();
        listRecordsIds.forEach(x -> {
            var index = indexOfRecordIdInList(x, currentListRecords);
            if (index != -1)
            {
                currentListRecords.remove(index);
            }
        });
    }

    private int indexOfRecordIdInList(Long id, List<ListRecord> listRecords)
    {
        int index = -1;
        if (id != null)
        {
            int size = listRecords.size();
            for (int i = 0; i < size; i++)
            {
                ListRecord listRecord = listRecords.get(i);
                if (id.equals(listRecord.getId()))
                {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    private void validateNewRecords(List<ListRecord> listData, GeneList geneList)
    {
        if (geneList.getListRecords() == null)
        {
            return;
        }
        Map<String, Long> sortedAccIdsInCurrentList = getAccIdHashesForGeneList(geneList);

        int size = listData.size();
        for (int i = 0; i < size; i++)
        {
            ListRecord listRecord = listData.get(i);
            if (listRecord.getId() == null)
            {
                List<String> symbols = new ArrayList<>();
                listRecord.getGenesByRecord().forEach(x -> symbols.add(x.getInputSymbolValue()));
                geneListRecordService.validateNewRecord(
                    listRecord, sortedAccIdsInCurrentList, symbols.toString());
            }
        }
    }

    public List<GeneListProjection> getGeneListProjectionsByConsortiumName(String consortiumName){
        return geneListRepository.findAllGeneListProjectionsByConsortiumName(consortiumName);
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
            geneListCsvConverter.buildListFromCsvContent(consortiumName, recordsByColumns, sortedAccIdsInCurrentList);
        validateNewCsvRecords(new ArrayList<>(listData), geneList, recordsByColumns);
        listData.forEach(x -> x.setGeneList(geneList));
        if (geneList.getListRecords() == null)
        {
            geneList.setListRecords(listData);
        }
        else
        {
            geneList.getListRecords().addAll(listData);
        }
        return geneList;
    }

    private Map<String, Long> getAccIdHashesForGeneList(GeneList geneList)
    {
        Map<String, Long> sortedAccIdsInCurrentList = new HashMap<>();
        var geneListRecords = geneList.getListRecords();
        if (geneListRecords != null)
        {
            geneListRecords.forEach(x ->
                sortedAccIdsInCurrentList.put(getAccIdHashByGeneListRecord(x), x.getId()));
        }

        return sortedAccIdsInCurrentList;
    }

    private void validateNewCsvRecords(
        List<ListRecord> listData,
        GeneList geneList, Map<String,
        List<String>> recordsByColumns)
    {
        if (geneList.getListRecords() == null)
        {
            return;
        }
        Map<String, Long> sortedAccIdsInCurrentList = getAccIdHashesForGeneList(geneList);

        List<String> geneColumnContent =
            recordsByColumns.get(GeneListCsvConverter.CSV_GENE_HEADER);

        int size = listData.size();
        for (int i = 0; i < size; i++)
        {
            ListRecord listRecord = listData.get(i);
            String geneSymbols = geneColumnContent.get(i);
            geneListRecordService.validateNewRecord(
                listRecord, sortedAccIdsInCurrentList, geneSymbols);
        }
    }

    private String getAccIdHashByGeneListRecord(ListRecord listRecord)
    {
        var genes = new ArrayList<>(listRecord.getGenesByRecord());
        genes.sort(new SortGeneByGeneListRecordByIndex());
        return genesByRecordToString(genes);
    }

    public String genesByRecordToString(List<GeneByListRecord> genes)
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
