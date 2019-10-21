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
package uk.ac.ebi.impc_prod_tracker.web.mapping.gene;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.exceptions.UserOperationFailedException;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.Gene;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention.ProjectIntention;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_gene.ProjectIntentionGene;
import uk.ac.ebi.impc_prod_tracker.service.gene.external_ref.GeneExternalService;
import uk.ac.ebi.impc_prod_tracker.service.gene.GeneService;
import uk.ac.ebi.impc_prod_tracker.service.project_intention.ProjectIntentionService;
import uk.ac.ebi.impc_prod_tracker.web.dto.gene.GeneDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.gene.ProjectIntentionGeneDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.project.intention.ProjectIntentionMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class ProjectIntentionGeneMapper
{
    private GeneMapper geneMapper;
    private ProjectIntentionMapper projectIntentionMapper;
    private GeneService geneService;
    private GeneExternalService geneExternalService;
    private ProjectIntentionService projectIntentionService;

    private static final String GENE_INTENTION_TYPE = "gene";

    private static final String GENE_NOT_EXIST_ERROR = "Gene [%s] does not exist.";

    public ProjectIntentionGeneMapper(
        GeneMapper geneMapper,
        ProjectIntentionMapper projectIntentionMapper,
        GeneService geneService,
        GeneExternalService geneExternalService, ProjectIntentionService projectIntentionService)
    {
        this.geneMapper = geneMapper;
        this.projectIntentionMapper = projectIntentionMapper;
        this.geneService = geneService;
        this.geneExternalService = geneExternalService;
        this.projectIntentionService = projectIntentionService;
    }

    public ProjectIntentionGeneDTO toDto(ProjectIntentionGene projectIntentionGene)
    {
        ProjectIntentionGeneDTO projectIntentionGeneDTO = new ProjectIntentionGeneDTO();
        projectIntentionGeneDTO.setGeneDTO(geneMapper.toDto(projectIntentionGene.getGene()));
        projectIntentionGeneDTO.setProjectIntentionDTO(
            projectIntentionMapper.toDto(projectIntentionGene.getProjectIntention()));
        return projectIntentionGeneDTO;
    }

    public List<ProjectIntentionGeneDTO> toDtos(Collection<ProjectIntentionGene> projectIntentionGenes)
    {
        List<ProjectIntentionGeneDTO> projectIntentionGeneDTOS = new ArrayList<>();
        if (projectIntentionGenes != null)
        {
            projectIntentionGenes.forEach(projectGene -> projectIntentionGeneDTOS.add(toDto(projectGene)));
        }
        return projectIntentionGeneDTOS;
    }

    public ProjectIntentionGene toEntity(ProjectIntentionGeneDTO projectIntentionGeneDTO)
    {
        ProjectIntentionGene projectIntentionGene = new ProjectIntentionGene();
        Gene gene = loadGene(projectIntentionGeneDTO.getGeneDTO());
        projectIntentionGene.setGene(gene);
        ProjectIntention projectIntention =
            projectIntentionMapper.toEntity(projectIntentionGeneDTO.getProjectIntentionDTO());
        projectIntention.setIntentionType(
            projectIntentionService.getIntentionTypeByName(GENE_INTENTION_TYPE));
        projectIntentionGene.setProjectIntention(projectIntention);
        return projectIntentionGene;
    }

    private Gene loadGene(GeneDTO geneDTO)
    {
        String accessionId = null;
        Gene gene = null;
        if (geneDTO != null)
        {
            accessionId = geneDTO.getAccId();
            gene = loadGeneFromLocalData(accessionId);
            if (gene == null)
            {
                gene = loadGeneFromExternalData(accessionId);
            }
        }
        if (gene == null)
        {
            throw new UserOperationFailedException(String.format(GENE_NOT_EXIST_ERROR, accessionId));
        }
        return gene;
    }

    private Gene loadGeneFromLocalData(String accessionId)
    {
        return geneService.getGeneByAccessionId(accessionId);
    }

    private Gene loadGeneFromExternalData(String accessionId)
    {
        return geneExternalService.getFromExternalGenesBySymbolOrAccId(accessionId);
    }
}
