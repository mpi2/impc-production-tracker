package org.gentar.biology.gene_list.record;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class ListRecordTypeService
{
    private final ListRecordTypeRepository listRecordTypeRepository;

    public ListRecordTypeService(ListRecordTypeRepository listRecordTypeRepository)
    {
        this.listRecordTypeRepository = listRecordTypeRepository;
    }

    @Cacheable("recordTypesByConsortium")
    public ListRecordType getRecordTypeByTypeNameAndConsortiumName(String name, String consortiumName)
    {
        return listRecordTypeRepository.findByNameAndGeneListConsortiumName(name, consortiumName);
    }
}
