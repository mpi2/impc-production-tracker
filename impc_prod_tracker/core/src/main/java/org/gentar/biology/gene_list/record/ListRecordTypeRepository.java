package org.gentar.biology.gene_list.record;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ListRecordTypeRepository extends CrudRepository<ListRecordType, Long>
{
    ListRecordType findByNameAndGeneListConsortiumName(String typeName, String consortiumName);
    List<ListRecordType> findAllByGeneListConsortiumName(String consortiumName);
}
