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
import org.gentar.biology.gene_list.record.GeneByListRecord;
import org.gentar.biology.gene_list.record.ListRecord;
import org.gentar.biology.gene_list.record.ListRecordSpecs;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.gentar.biology.gene_list.record.ListRecordRepository;
import org.gentar.biology.gene_list.record.SortGeneByGeneListRecordByIndex;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class GeneListRecordService
{
    private final ListRecordRepository listRecordRepository;

    public GeneListRecordService(ListRecordRepository listRecordRepository)
    {
        this.listRecordRepository = listRecordRepository;
    }

    public ListRecord getGeneListRecordById(Long id)
    {
        return listRecordRepository.findById(id).orElse(null);
    }

    public Page<ListRecord> getAllByConsortium(Pageable pageable, String consortiumName)
    {
        return listRecordRepository.findAllByGeneListConsortiumName(pageable, consortiumName);
    }

    // Method to use when no pagination is applicable (export).
    public List<ListRecord> getAllNotPaginated(GeneListFilter filter)
    {
        return listRecordRepository.findAll(buildSpecs(filter));
    }

    public Page<ListRecord> getAllBySpecs(Pageable pageable, GeneListFilter filter)
    {
        return listRecordRepository.findAll(buildSpecs(filter), pageable);
    }

    public Page<ListRecord> getPublicRecordsByConsortium(Pageable pageable, Long consortiumId)
    {
        return listRecordRepository.findPublicByConsortiumId(pageable, consortiumId);
    }

    public String genesByRecordToString(Collection<GeneByListRecord> genes)
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

    private Specification<ListRecord> buildSpecs(GeneListFilter filter)
    {
        Specification<ListRecord> specifications =
            Specification.where(
                ListRecordSpecs.withConsortiumName(filter.getConsortiumName()))
                    .and(ListRecordSpecs.withAccIds(filter.getAccIds()))
                    .and(ListRecordSpecs.withVisible(filter.getVisible()));
        return specifications;
    }

    private String getAccIdHashByGeneListRecord(ListRecord listRecord)
    {
        var genes = new ArrayList<>(listRecord.getGenesByRecord());
        genes.sort(new SortGeneByGeneListRecordByIndex());
        return genesByRecordToString(genes);
    }

    /**
     *
     * @param listRecord The new record in the a gene list.
     * @param geneRecordHashes A list of strings with the genes present in the list
     *                                  so the search for duplicates can be done quicker.
     * @param geneSymbols The label to show if an exception occurs.
     */
    public void validateNewRecord(
        ListRecord listRecord, Map<String, Long> geneRecordHashes, String geneSymbols)
    {
        List<GeneByListRecord> genes = new ArrayList<>(listRecord.getGenesByRecord());
        genes.sort(new SortGeneByGeneListRecordByIndex());
        String hashNewRecord = getAccIdHashByGeneListRecord(listRecord);
        Long id = geneRecordHashes.get(hashNewRecord);
        if (id != null && !id.equals(listRecord.getId()))
        {
            throw new UserOperationFailedException(
                "Gene(s) [" + geneSymbols + "] already in this list.");
        }
    }
}
