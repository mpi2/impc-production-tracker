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
import org.springframework.stereotype.Component;
import org.gentar.biology.project.assignment_status.AssignmentStatus;
import org.gentar.biology.gene_list.record.GeneByGeneListRecord;
import org.gentar.biology.gene_list.record.GeneListRecord;
import org.gentar.biology.project.Project;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class GeneListRecordMapper implements Mapper<GeneListRecord, GeneListRecordDTO>
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
            projectByGeneSummaryDTO.setAssignmentStatusName(assignmentStatus.getName());
        }
        return projectByGeneSummaryDTO;
    }

    public GeneListRecord toEntity(GeneListRecordDTO geneListRecordDTO)
    {
        GeneListRecord geneListRecord;
        Long id = geneListRecordDTO.getId();
        if (id != null)
        {
            geneListRecord = geneListRecordService.getGeneListRecordById(id);
        }
        else
        {
            geneListRecord = new GeneListRecord();
            Set<GeneByGeneListRecord> geneByGeneListRecords = new HashSet<>();
            geneListRecord.setGenesByRecord(geneByGeneListRecords);
        }
        geneListRecord.setNote(geneListRecordDTO.getNote());
        addGeneByGeneListRecords(geneListRecord, geneListRecordDTO);
        return geneListRecord;
    }

    private void addGeneByGeneListRecords(
        GeneListRecord geneListRecord, GeneListRecordDTO geneListRecordDTO)
    {
        var geneEntities = geneByGeneListRecordMapper.toEntities(geneListRecordDTO.getGenes());
        Set<GeneByGeneListRecord> geneByGeneListRecords = new HashSet<>(geneEntities);
        if (geneListRecord.getGenesByRecord() == null)
        {
            geneListRecord.setGenesByRecord(geneByGeneListRecords);
        }
        else
        {
            geneListRecord.getGenesByRecord().addAll(geneByGeneListRecords);
        }
        AtomicInteger index = new AtomicInteger();
        geneByGeneListRecords.forEach(x -> {
            x.setGeneListRecord(geneListRecord);
            x.setIndex(index.getAndIncrement());
        });
    }
}
