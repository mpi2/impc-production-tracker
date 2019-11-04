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
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.biology.target_gene_list.ConsortiumList;
import uk.ac.ebi.impc_prod_tracker.service.biology.target_gene_list.ProjectsByGroupOfGenesFinder;
import uk.ac.ebi.impc_prod_tracker.web.dto.target_gene_list.ConsortiumListDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.target_gene_list.ProjectByGeneSummaryDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.EntityMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class ConsortiumListMapper implements Mapper<ConsortiumList, ConsortiumListDTO>
{
    private TargetGroupMapper targetGroupMapper;
    private ProjectsByGroupOfGenesFinder projectsByGroupOfGenesFinder;
    private EntityMapper entityMapper;

    public ConsortiumListMapper(
        TargetGroupMapper targetGroupMapper,
        ProjectsByGroupOfGenesFinder projectsByGroupOfGenesFinder, EntityMapper entityMapper)
    {
        this.targetGroupMapper = targetGroupMapper;
        this.projectsByGroupOfGenesFinder = projectsByGroupOfGenesFinder;
        this.entityMapper = entityMapper;
    }

    public ConsortiumListDTO toDto(ConsortiumList consortiumList)
    {
        ConsortiumListDTO consortiumListDTO = new ConsortiumListDTO();
        consortiumListDTO.setId(consortiumList.getId());
        consortiumListDTO.setNote(consortiumList.getNote());
        consortiumListDTO.setTargetDTOS(
            targetGroupMapper.toDtos(consortiumList.getTargetGroups()));
        List<Project> projects =
            projectsByGroupOfGenesFinder.findProjectsByGenes(consortiumList.getTargetGroups());
        consortiumListDTO.setProjectByGeneSummaryDTOS(projectsToSummariesDtos(projects));
        return consortiumListDTO;
    }

    private List<ProjectByGeneSummaryDTO> projectsToSummariesDtos(Collection<Project> projects)
    {
        List<ProjectByGeneSummaryDTO> projectByGeneSummaryDTOS = new ArrayList<>();
        if (projects != null)
        {
            projects.forEach(x -> projectByGeneSummaryDTOS.add(projectToSummaryDto(x)));
        }
        return projectByGeneSummaryDTOS;
    }

    private ProjectByGeneSummaryDTO projectToSummaryDto(Project project)
    {
        return entityMapper.toTarget(project, ProjectByGeneSummaryDTO.class);

    }
}
