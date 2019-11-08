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
package uk.ac.ebi.impc_prod_tracker.web.mapping.gene_list;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.Gene;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene_list.gene_list_record.GeneByGeneListRecord;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene_list.gene_list_record.GeneByGeneListRecordDTO;
import uk.ac.ebi.impc_prod_tracker.service.biology.gene.external_ref.GeneExternalService;
import uk.ac.ebi.impc_prod_tracker.web.mapping.Mapper;

@Component
public class GeneByGeneListRecordMapper implements Mapper<GeneByGeneListRecord, GeneByGeneListRecordDTO>
{
    private GeneExternalService geneExternalService;

    public GeneByGeneListRecordMapper(GeneExternalService geneExternalService)
    {
        this.geneExternalService = geneExternalService;
    }

    @Override
    public GeneByGeneListRecordDTO toDto(GeneByGeneListRecord entity)
    {
        GeneByGeneListRecordDTO geneByGeneListRecordDTO = new GeneByGeneListRecordDTO();
        geneByGeneListRecordDTO.setAccId(entity.getAccId());
        if (entity.getAccId() != null)
        {
            Gene gene = geneExternalService.getGeneFromExternalDataBySymbolOrAccId(entity.getAccId());
            geneByGeneListRecordDTO.setName(gene.getName());
            geneByGeneListRecordDTO.setSymbol(gene.getSymbol());
        }

        return geneByGeneListRecordDTO;
    }

    @Override
    public GeneByGeneListRecord toEntity(GeneByGeneListRecordDTO geneByGeneListRecordDTO)
    {
        return null;
    }
}
