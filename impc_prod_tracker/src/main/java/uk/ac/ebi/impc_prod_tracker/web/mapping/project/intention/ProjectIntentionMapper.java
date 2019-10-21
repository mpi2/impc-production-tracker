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
package uk.ac.ebi.impc_prod_tracker.web.mapping.project.intention;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention.ProjectIntention;
import uk.ac.ebi.impc_prod_tracker.web.dto.intention.ProjectIntentionDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.EntityMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.allele.AlleleTypeMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.allele_categorization.AlleleCategorizationMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.molecular_mutation.MolecularMutationTypeMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ProjectIntentionMapper
{
    private EntityMapper entityMapper;
    private AlleleTypeMapper alleleTypeMapper;
    private AlleleCategorizationMapper alleleCategorizationMapper;
    private MolecularMutationTypeMapper molecularMutationTypeMapper;

    public ProjectIntentionMapper(
        EntityMapper entityMapper,
        AlleleTypeMapper alleleTypeMapper,
        AlleleCategorizationMapper alleleCategorizationMapper,
        MolecularMutationTypeMapper molecularMutationTypeMapper)
    {
        this.entityMapper = entityMapper;
        this.alleleTypeMapper = alleleTypeMapper;
        this.alleleCategorizationMapper = alleleCategorizationMapper;
        this.molecularMutationTypeMapper = molecularMutationTypeMapper;
    }

    public ProjectIntentionDTO toDto(ProjectIntention projectIntention)
    {
        ProjectIntentionDTO projectIntentionDTO =
            entityMapper.toTarget(projectIntention, ProjectIntentionDTO.class);
        projectIntentionDTO.setAlleleCategorizationDTOS(
            alleleCategorizationMapper.toDtos(projectIntention.getAlleleCategorizations()));
        return projectIntentionDTO;
    }

    public List<ProjectIntentionDTO> toDtos(Set<ProjectIntention> projectIntention)
    {
        List<ProjectIntentionDTO> intentionDTOS = new ArrayList<>();
        if (projectIntention != null)
        {
            projectIntention.forEach(x -> intentionDTOS.add(toDto(x)));
        }
        return intentionDTOS;
    }
    public ProjectIntention toEntity(ProjectIntentionDTO projectIntentionDTO)
    {
        ProjectIntention projectIntention =
            entityMapper.toTarget(projectIntentionDTO, ProjectIntention.class);
        projectIntention.setAlleleType(
            alleleTypeMapper.toEntity(projectIntentionDTO.getAlleleTypeName()));
        projectIntention.setMolecularMutationType(
            molecularMutationTypeMapper.toEntity(projectIntentionDTO.getMolecularMutationTypeName()));
        return projectIntention;
    }

    public Set<ProjectIntention> toEntities(Collection<ProjectIntentionDTO> projectIntentionDTOS)
    {
        Set<ProjectIntention> projectIntentions = new HashSet<>();
        if (projectIntentionDTOS != null)
        {
            projectIntentionDTOS.forEach(x -> projectIntentions.add(toEntity(x)));
        }
        return projectIntentions;
    }
}
