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

import org.gentar.biology.project.search.filter.ProjectFilter;
import org.gentar.biology.project.search.ProjectSearcherService;
import org.gentar.biology.project.search.Search;
import org.gentar.biology.project.search.SearchReport;
import org.gentar.biology.project.search.SearchType;
import org.springframework.stereotype.Component;
import org.gentar.biology.gene_list.record.GeneByListRecord;
import org.gentar.biology.project.Project;
import java.util.ArrayList;
import java.util.Collections;
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

    public List<Project> findProjectsByGenes(Set<GeneByListRecord> geneByListRecords)
    {
        Set<Project> projects = new HashSet<>();
        List<String> ids = new ArrayList<>();
        if (geneByListRecords != null && !geneByListRecords.isEmpty())
        {
            geneByListRecords.forEach(x -> ids.add(x.getAccId()));
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
        return filterExactMatches(ids, projects);
    }
    
    private List<Project> filterExactMatches(List<String> ids, Set<Project> projects)
    {
        List<Project> filteredProjects = new ArrayList<>();
        projects.forEach(p -> {
            if (hasExactlyTheseGeneIntentions(ids, p))
            {
                filteredProjects.add(p);
            }
        });
        return filteredProjects;
    }
    private boolean hasExactlyTheseGeneIntentions(List<String> geneIntentionsIds, Project project)
    {
        Collections.sort(geneIntentionsIds);
        List<String> intentionsAsAccIds = getIntentionsAsAccIds(project);
        Collections.sort(intentionsAsAccIds);
        return geneIntentionsIds.equals(intentionsAsAccIds);
    }

    private List<String> getIntentionsAsAccIds(Project project)
    {
        List<String> intentionsAsAccIds = new ArrayList<>();
        var intentions = project.getProjectIntentions();
        intentions.forEach(i ->
        {
            var geneIntention = i.getProjectIntentionGene();
            intentionsAsAccIds.add(geneIntention.getGene().getAccId());
        });
        return intentionsAsAccIds;
    }
}
