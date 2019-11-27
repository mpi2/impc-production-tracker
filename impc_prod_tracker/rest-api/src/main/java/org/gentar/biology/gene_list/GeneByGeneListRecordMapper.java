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

import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.biology.gene.external_ref.GeneExternalService;
import org.gentar.biology.gene_list.GeneByGeneListRecordService;
import org.gentar.biology.gene_list.GeneByGeneListRecordDTO;
import org.gentar.Mapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.gene_list.record.GeneByGeneListRecord;

@Component
public class GeneByGeneListRecordMapper
    implements Mapper<GeneByGeneListRecord, GeneByGeneListRecordDTO>
{
    private GeneExternalService geneExternalService;
    private GeneByGeneListRecordService geneByGeneListRecordService;

    public GeneByGeneListRecordMapper(
        GeneExternalService geneExternalService,
        GeneByGeneListRecordService geneByGeneListRecordService)
    {
        this.geneExternalService = geneExternalService;
        this.geneByGeneListRecordService = geneByGeneListRecordService;
    }

    @Override
    public GeneByGeneListRecordDTO toDto(GeneByGeneListRecord entity)
    {
        GeneByGeneListRecordDTO geneByGeneListRecordDTO = new GeneByGeneListRecordDTO();
        geneByGeneListRecordDTO.setAccId(entity.getAccId());
        geneByGeneListRecordDTO.setId(entity.getId());
        geneByGeneListRecordDTO.setIndex(entity.getIndex());
        if (entity.getAccId() != null)
        {
            Gene gene =
                geneExternalService.getGeneFromExternalDataBySymbolOrAccId(entity.getAccId());
            validateGeneExist(gene, entity.getAccId());
            geneByGeneListRecordDTO.setName(gene.getName());
            geneByGeneListRecordDTO.setSymbol(gene.getSymbol());
        }

        return geneByGeneListRecordDTO;
    }

    private void validateGeneExist(Gene gene, String accIdOrSymbol)
    {
        if (gene == null)
        {
            throw new UserOperationFailedException(
                accIdOrSymbol + " is not a valid gene symbol or accession id.");
        }
    }

    @Override
    public GeneByGeneListRecord toEntity(GeneByGeneListRecordDTO geneByGeneListRecordDTO)
    {
        GeneByGeneListRecord geneByGeneListRecord;
        Long id = geneByGeneListRecordDTO.getId();
        if (id == null)
        {
            geneByGeneListRecord = new GeneByGeneListRecord();
        }
        else
        {
            geneByGeneListRecord = geneByGeneListRecordService.findById(id);
        }
        geneByGeneListRecord.setAccId(geneByGeneListRecordDTO.getAccId());
        String newSymbol = geneByGeneListRecordDTO.getSymbol();
        Gene gene =
            geneExternalService.getGeneFromExternalDataBySymbolOrAccId(newSymbol);
        validateGeneExist(gene, newSymbol);
        geneByGeneListRecord.setInputSymbolValue(geneByGeneListRecordDTO.getSymbol());
        geneByGeneListRecord.setAccId(gene.getAccId());
        return geneByGeneListRecord;
    }
}
