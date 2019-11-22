/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package org.gentar.web.mapping.gene;

import org.gentar.web.dto.gene.GeneDTO;
import org.gentar.web.mapping.EntityMapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.gene.Gene;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GeneMapper
{
    private EntityMapper entityMapper;
    private static final String MGI_URL = "http://www.mousephenotype.org/data/genes/";

    public GeneMapper(EntityMapper entityMapper)
    {
        this.entityMapper = entityMapper;
    }

    public GeneDTO toDto(Gene gene)
    {
        GeneDTO geneDTO = entityMapper.toTarget(gene, GeneDTO.class);
        geneDTO.setExternalLink(MGI_URL + gene.getAccId());
        return geneDTO;
    }

    public List<GeneDTO> toDtos(Collection<Gene> genes)
    {
        return entityMapper.toTargets(genes, GeneDTO.class);
    }

    public Gene toEntity(GeneDTO geneDTO)
    {
        return entityMapper.toTarget(geneDTO, Gene.class);
    }

    public Set<Gene> toEntity(Collection<GeneDTO> geneDTOS)
    {
        Set<Gene> genes = new HashSet<>(entityMapper.toTargets(geneDTOS, Gene.class));
        return genes;
    }
}
