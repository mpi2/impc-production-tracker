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
import org.gentar.biology.gene_list.record.ListRecordType;
import org.gentar.biology.gene_list.record.ListRecordTypeService;
import org.springframework.stereotype.Component;
import org.gentar.biology.project.Project;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ListRecordMapper implements Mapper<ListRecord, ListRecordDTO>
{
    private final GeneByListRecordMapper geneByListRecordMapper;
    private final ProjectsByGroupOfGenesFinder projectsByGroupOfGenesFinder;
    private final ProjectByGeneSummaryMapper projectByGeneSummaryMapper;
    private final ListRecordTypeService listRecordTypeService;

    public ListRecordMapper(
        GeneByListRecordMapper geneByListRecordMapper,
        ProjectsByGroupOfGenesFinder projectsByGroupOfGenesFinder,
        ProjectByGeneSummaryMapper projectByGeneSummaryMapper,
        ListRecordTypeService listRecordTypeService)
    {
        this.geneByListRecordMapper = geneByListRecordMapper;
        this.projectsByGroupOfGenesFinder = projectsByGroupOfGenesFinder;
        this.projectByGeneSummaryMapper = projectByGeneSummaryMapper;
        this.listRecordTypeService = listRecordTypeService;
    }

    @Override
    public ListRecordDTO toDto(ListRecord listRecord)
    {
        ListRecordDTO listRecordDTO = new ListRecordDTO();
        listRecordDTO.setId(listRecord.getId());
        listRecordDTO.setNote(listRecord.getNote());
        setRecordTypes(listRecordDTO, listRecord);
        listRecordDTO.setGenes(
            geneByListRecordMapper.toDtos(listRecord.getGenesByRecord()));
        List<Project> projects =
            projectsByGroupOfGenesFinder.findProjectsByGenes(listRecord.getGenesByRecord());
        listRecordDTO.setProjectByGeneSummaryDTOS(projectsToSummariesDtos(projects));

        return listRecordDTO;
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
        return projectByGeneSummaryMapper.toDto(project);
    }

    private void setRecordTypes(ListRecordDTO listRecordDTO, ListRecord listRecord)
    {
        List<String> recordTypeNames = new ArrayList<>();
        Set<ListRecordType> listRecordTypes = listRecord.getListRecordTypes();
        if (listRecordTypes != null)
        {
            listRecordTypes.forEach(x -> recordTypeNames.add(x.getName()));
        }
        listRecordDTO.setRecordTypes(recordTypeNames);
    }

    @Override
    public ListRecord toEntity(ListRecordDTO listRecordDTO)
    {
        ListRecord listRecord = new ListRecord();
        listRecord.setId(listRecordDTO.getId());
        listRecord.setNote(listRecordDTO.getNote());
        addGeneByListRecords(listRecord, listRecordDTO);
        addRecordTypes(listRecord, listRecordDTO);
        return listRecord;
    }

    private void addGeneByListRecords(
        ListRecord listRecord, ListRecordDTO listRecordDTO)
    {
        var geneEntities =
            geneByListRecordMapper.toEntities(listRecordDTO.getGenes());
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

    private void addRecordTypes(ListRecord listRecord, ListRecordDTO listRecordDTO)
    {
        String consortiumName = listRecordDTO.getConsortiumName();
        Set<ListRecordType> listRecordTypes = new HashSet<>();
        List<String> recordTypesNames = listRecordDTO.getRecordTypes();
        if (recordTypesNames != null)
        {
            recordTypesNames.forEach(x -> {
                ListRecordType listRecordType =
                    listRecordTypeService.getRecordTypeByTypeNameAndConsortiumName(x, consortiumName);
                listRecordTypes.add(listRecordType);
            });
        }
        listRecord.setListRecordTypes(listRecordTypes);
    }
}
