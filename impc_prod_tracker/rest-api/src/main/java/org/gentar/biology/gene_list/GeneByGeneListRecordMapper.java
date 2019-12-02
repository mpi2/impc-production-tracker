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

import org.gentar.biology.gene_list.record.GeneByListRecord;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.biology.gene.external_ref.GeneExternalService;
import org.gentar.Mapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.gene.Gene;

@Component
public class GeneByGeneListRecordMapper
    implements Mapper<GeneByListRecord, GeneByListRecordDTO>
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
    public GeneByListRecordDTO toDto(GeneByListRecord entity)
    {
        GeneByListRecordDTO geneByListRecordDTO = new GeneByListRecordDTO();
        geneByListRecordDTO.setAccId(entity.getAccId());
        geneByListRecordDTO.setId(entity.getId());
        geneByListRecordDTO.setIndex(entity.getIndex());
        if (entity.getAccId() != null)
        {
            Gene gene =
                geneExternalService.getGeneFromExternalDataBySymbolOrAccId(entity.getAccId());
            validateGeneExist(gene, entity.getAccId());
            geneByListRecordDTO.setName(gene.getName());
            geneByListRecordDTO.setSymbol(gene.getSymbol());
        }

        return geneByListRecordDTO;
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
    public GeneByListRecord toEntity(GeneByListRecordDTO geneByListRecordDTO)
    {
        GeneByListRecord geneByListRecord;
        Long id = geneByListRecordDTO.getId();
        if (id == null)
        {
            geneByListRecord = new GeneByListRecord();
        }
        else
        {
            geneByListRecord = geneByGeneListRecordService.findById(id);
        }
        geneByListRecord.setAccId(geneByListRecordDTO.getAccId());
        String newSymbol = geneByListRecordDTO.getSymbol();
        Gene gene =
            geneExternalService.getGeneFromExternalDataBySymbolOrAccId(newSymbol);
        validateGeneExist(gene, newSymbol);
        geneByListRecord.setInputSymbolValue(geneByListRecordDTO.getSymbol());
        geneByListRecord.setAccId(gene.getAccId());
        return geneByListRecord;
    }
}
