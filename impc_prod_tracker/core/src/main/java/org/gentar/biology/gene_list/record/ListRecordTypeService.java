package org.gentar.biology.gene_list.record;

import org.gentar.biology.gene_list.GeneListRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ListRecordTypeService
{
    private final ListRecordTypeRepository listRecordTypeRepository;
    private final ListRecordRepository listRecordRepository;
    private final GeneListRepository geneListRepository;

    public ListRecordTypeService(
            ListRecordTypeRepository listRecordTypeRepository,
            ListRecordRepository listRecordRepository, GeneListRepository geneListRepository)
    {
        this.listRecordTypeRepository = listRecordTypeRepository;
        this.listRecordRepository = listRecordRepository;
        this.geneListRepository = geneListRepository;
    }

    @Cacheable("recordTypesByConsortium")
    public ListRecordType getRecordTypeByTypeNameAndConsortiumName(String name, String consortiumName)
    {
        return listRecordTypeRepository.findByNameAndGeneListConsortiumName(name, consortiumName);
    }

    public Map<String, List<String>> getRecordTypesByConsortium()
    {
        Map<String, List<String>> map = new HashMap<>();
        var geneLists = geneListRepository.findAll();
        geneLists.forEach(geneList -> {
            String consortiumName = geneList.getConsortium().getName();
            var recordTypes =
                    listRecordTypeRepository.findAllByGeneListConsortiumName(consortiumName);
            List<String> workGroupsNames = new ArrayList<>();
            map.put(
                    consortiumName,
                    recordTypes.stream().map(ListRecordType::getName).collect(Collectors.toList()));
        });
        return map;
    }

    public Map<Long, List<String>> getRecordTypesByListRecord()
    {
        Map<Long, List<String>> map = new HashMap<>();
        var listRecords = listRecordRepository.findAll();
        listRecords.forEach(listRecord -> {
            Set<ListRecordType> listRecordTypes = listRecord.getListRecordTypes();
            List<String> listRecordTypeNames = listRecordTypes.stream().map(ListRecordType::getName).collect(Collectors.toList());
            map.put(listRecord.getId(), listRecordTypeNames);
        });
        return map;
    }
}
