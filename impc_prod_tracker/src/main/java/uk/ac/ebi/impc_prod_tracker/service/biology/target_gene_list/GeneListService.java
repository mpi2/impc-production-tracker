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
package uk.ac.ebi.impc_prod_tracker.service.biology.target_gene_list;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.exceptions.UserOperationFailedException;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.Gene;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene_list.GeneList;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene_list.GeneListRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene_list.gene_list_record.GeneByGeneListRecord;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene_list.gene_list_record.GeneListRecord;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene_list.gene_list_record.GeneListRecordRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.consortium.Consortium;
import uk.ac.ebi.impc_prod_tracker.service.biology.gene.external_ref.GeneExternalService;
import uk.ac.ebi.impc_prod_tracker.service.organization.consortium.ConsortiumService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Component
public class GeneListService
{
    private GeneListRepository geneListRepository;
    private GeneListRecordRepository geneListRecordRepository;
    private GeneListCsvConverter geneListCsvConverter;
    private GeneExternalService geneExternalService;
    private ConsortiumService consortiumService;

    public GeneListService(
        GeneListRepository geneListRepository,
        GeneListRecordRepository geneListRecordRepository,
        GeneListCsvConverter geneListCsvConverter, GeneExternalService geneExternalService, ConsortiumService consortiumService)
    {
        this.geneListRepository = geneListRepository;
        this.geneListRecordRepository = geneListRecordRepository;
        this.geneListCsvConverter = geneListCsvConverter;
        this.geneExternalService = geneExternalService;
        this.consortiumService = consortiumService;
    }

    public GeneList getGeneListByConsortium(Consortium consortium)
    {
        return geneListRepository.findByConsortium(consortium);
    }

    public Page<GeneListRecord> getByConsortium(Pageable pageable, String consortiumName)
    {
        return geneListRecordRepository.findAllByGeneListConsortiumName(pageable, consortiumName);
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
        System.out.println(geneList);
    }

    public GeneList buildListFromCsvContent(
        String consortiumName, List<List<String>> csvContent)
    {
        Consortium consortium = consortiumService.getConsortiumByNameOrThrowException(consortiumName);
        final GeneList geneList = getOrCreateGeneList(consortium);
        Set<GeneListRecord> listData = geneListCsvConverter.processCsvContent(csvContent);
        if (listData != null)
        {
            listData.forEach(x -> x.setGeneList(geneList));
        }
        geneList.setGeneListRecords(listData);
        return geneList;
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
