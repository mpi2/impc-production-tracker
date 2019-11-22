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
package org.gentar.service.biology.gene_list;

import org.gentar.service.biology.project.search.ProjectFilter;
import org.gentar.service.biology.project.search.ProjectSearcherService;
import org.gentar.service.biology.project.search.Search;
import org.gentar.service.biology.project.search.SearchReport;
import org.gentar.service.biology.project.search.SearchType;
import org.springframework.stereotype.Component;
import org.gentar.biology.gene_list.record.GeneByGeneListRecord;
import org.gentar.biology.project.Project;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ProjectsByGroupOfGenesFinder
{
    private ProjectSearcherService projectSearcherService;

    public ProjectsByGroupOfGenesFinder(ProjectSearcherService projectSearcherService)
    {
        this.projectSearcherService = projectSearcherService;
    }

    public List<Project> findProjectsByGenes(Set<GeneByGeneListRecord> geneByGeneListRecords)
    {
        Set<Project> projects = new HashSet<>();
        if (!geneByGeneListRecords.isEmpty())
        {
            List<String> ids = new ArrayList<>();
            if (geneByGeneListRecords != null)
            {
                geneByGeneListRecords.forEach(x -> ids.add(x.getAccId()));
            }
            Search search = new Search(SearchType.BY_GENE.getName(), ids, ProjectFilter.getInstance());
            SearchReport searchReport = projectSearcherService.executeSearch(search);
            if (searchReport.getResults() != null)
            {
                searchReport.getResults().forEach(x -> {
                    Project p = x.getProject();
                    if (p != null)
                    {
                        projects.add(p);
                    }
                });
            }
        }
        return filterExactMatches(projects);
    }

    private List<Project> filterExactMatches(Set<Project> projects)
    {
        return new ArrayList<>(projects);
    }
}
