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
package uk.ac.ebi.impc_prod_tracker.web.mapping.target_gene_list;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.Gene;
import uk.ac.ebi.impc_prod_tracker.data.biology.target_gene_list.target_group.TargetGroup;
import uk.ac.ebi.impc_prod_tracker.service.biology.gene.external_ref.GeneExternalService;
import uk.ac.ebi.impc_prod_tracker.web.dto.target_gene_list.TargetDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.Mapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.gene.GeneMapper;

@Component
public class TargetGroupMapper implements Mapper<TargetGroup, TargetDTO>
{
    private GeneExternalService geneExternalService;
    private GeneMapper geneMapper;

    public TargetGroupMapper(GeneExternalService geneExternalService, GeneMapper geneMapper)
    {
        this.geneExternalService = geneExternalService;
        this.geneMapper = geneMapper;
    }

    public TargetDTO toDto(TargetGroup targetGroup)
    {
        TargetDTO targetDTO = new TargetDTO();
        Gene gene = getGeneByAccessionId(targetGroup.getAccId());
        targetDTO.setGeneDTO(geneMapper.toDto(gene));
        return targetDTO;
    }

    private Gene getGeneByAccessionId(String accId)
    {
        return geneExternalService.getGeneFromExternalDataBySymbolOrAccId(accId);
    }
}
