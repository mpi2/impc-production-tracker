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

import org.gentar.Mapper;
import org.gentar.biology.gene_list.record.GeneByListRecord;
import org.gentar.biology.gene_list.record.ListRecord;
import org.springframework.stereotype.Component;
import org.gentar.biology.project.assignment_status.AssignmentStatus;
import org.gentar.biology.project.Project;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class GeneListRecordMapper implements Mapper<ListRecord, GeneListRecordDTO>
{
    private GeneByGeneListRecordMapper geneByGeneListRecordMapper;
    private ProjectsByGroupOfGenesFinder projectsByGroupOfGenesFinder;
    private GeneListRecordService geneListRecordService;

    public GeneListRecordMapper(
        GeneByGeneListRecordMapper geneByGeneListRecordMapper,
        ProjectsByGroupOfGenesFinder projectsByGroupOfGenesFinder, GeneListRecordService geneListRecordService)
    {
        this.geneByGeneListRecordMapper = geneByGeneListRecordMapper;
        this.projectsByGroupOfGenesFinder = projectsByGroupOfGenesFinder;
        this.geneListRecordService = geneListRecordService;
    }

    @Override
    public GeneListRecordDTO toDto(ListRecord listRecord)
    {
        GeneListRecordDTO geneListRecordDTO = new GeneListRecordDTO();
        geneListRecordDTO.setId(listRecord.getId());
        geneListRecordDTO.setNote(listRecord.getNote());
        geneListRecordDTO.setGenes(
            geneByGeneListRecordMapper.toDtos(listRecord.getGenesByRecord()));
        List<Project> projects =
            projectsByGroupOfGenesFinder.findProjectsByGenes(listRecord.getGenesByRecord());
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
            projectByGeneSummaryDTO.setAssignmentStatusName(assignmentStatus.getName());
        }
        return projectByGeneSummaryDTO;
    }

    public ListRecord toEntity(GeneListRecordDTO geneListRecordDTO)
    {
        ListRecord listRecord;
        Long id = geneListRecordDTO.getId();
        if (id != null)
        {
            listRecord = geneListRecordService.getGeneListRecordById(id);
        }
        else
        {
            listRecord = new ListRecord();
            Set<GeneByListRecord> geneByListRecords = new HashSet<>();
            listRecord.setGenesByRecord(geneByListRecords);
        }
        listRecord.setNote(geneListRecordDTO.getNote());
        addGeneByGeneListRecords(listRecord, geneListRecordDTO);
        return listRecord;
    }

    private void addGeneByGeneListRecords(
        ListRecord listRecord, GeneListRecordDTO geneListRecordDTO)
    {
        var geneEntities = geneByGeneListRecordMapper.toEntities(geneListRecordDTO.getGenes());
        Set<GeneByListRecord> geneByListRecords = new HashSet<>(geneEntities);
        if (listRecord.getGenesByRecord() == null)
        {
            listRecord.setGenesByRecord(geneByListRecords);
        }
        else
        {
            listRecord.getGenesByRecord().addAll(geneByListRecords);
        }
        AtomicInteger index = new AtomicInteger();
        geneByListRecords.forEach(x -> {
            x.setListRecord(listRecord);
            x.setIndex(index.getAndIncrement());
        });
    }
}
