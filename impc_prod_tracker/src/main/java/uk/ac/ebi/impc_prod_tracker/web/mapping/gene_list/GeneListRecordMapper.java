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
import uk.ac.ebi.impc_prod_tracker.data.biology.assignment_status.AssignmentStatus;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene_list.gene_list_record.GeneListRecord;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.service.biology.target_gene_list.ProjectsByGroupOfGenesFinder;
import uk.ac.ebi.impc_prod_tracker.web.dto.gene_list.GeneListRecordDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.gene_list.ProjectByGeneSummaryDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.Mapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class GeneListRecordMapper implements Mapper<GeneListRecord, GeneListRecordDTO>
{
    private GeneByGeneListRecordMapper geneByGeneListRecordMapper;
    private ProjectsByGroupOfGenesFinder projectsByGroupOfGenesFinder;

    public GeneListRecordMapper(
        GeneByGeneListRecordMapper geneByGeneListRecordMapper,
        ProjectsByGroupOfGenesFinder projectsByGroupOfGenesFinder)
    {
        this.geneByGeneListRecordMapper = geneByGeneListRecordMapper;
        this.projectsByGroupOfGenesFinder = projectsByGroupOfGenesFinder;
    }

    @Override
    public GeneListRecordDTO toDto(GeneListRecord geneListRecord)
    {
        GeneListRecordDTO geneListRecordDTO = new GeneListRecordDTO();
        geneListRecordDTO.setId(geneListRecord.getId());
        geneListRecordDTO.setNote(geneListRecord.getNote());
        geneListRecordDTO.setGenes(
            geneByGeneListRecordMapper.toDtos(geneListRecord.getGenesByRecord()));
        List<Project> projects =
            projectsByGroupOfGenesFinder.findProjectsByGenes(geneListRecord.getGenesByRecord());
        geneListRecordDTO.setProjectByGeneSummaryDTOS(projectsToSummariesDtos(projects));

        return geneListRecordDTO;
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
        ProjectByGeneSummaryDTO projectByGeneSummaryDTO = new ProjectByGeneSummaryDTO();
        projectByGeneSummaryDTO.setTpn(project.getTpn());
        AssignmentStatus assignmentStatus = project.getAssignmentStatus();
        if (assignmentStatus != null)
        {
            projectByGeneSummaryDTO.setAssigmentStatusName(assignmentStatus.getName());
        }
        return projectByGeneSummaryDTO;
    }
}
