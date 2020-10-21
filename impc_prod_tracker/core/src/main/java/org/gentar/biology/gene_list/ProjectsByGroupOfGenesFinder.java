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

import org.gentar.biology.project.ProjectService;
import org.gentar.biology.project.search.filter.ProjectFilterBuilder;
import org.springframework.stereotype.Component;
import org.gentar.biology.gene_list.record.GeneByListRecord;
import org.gentar.biology.project.Project;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
public class ProjectsByGroupOfGenesFinder
{
    private final ProjectService projectService;

    public ProjectsByGroupOfGenesFinder(ProjectService projectService)
    {
        this.projectService = projectService;
    }

    public List<Project> findProjectsByGenes(Set<GeneByListRecord> geneByListRecords)
    {
        List<Project> projects = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        if (geneByListRecords != null && !geneByListRecords.isEmpty())
        {
            geneByListRecords.forEach(x -> ids.add(x.getAccId()));
            projects =
                projectService.getProjectsWithoutOrthologs(
                    ProjectFilterBuilder.getInstance().withGenes(ids).build());
        }
        return filterExactMatches(ids, projects);
    }

    private List<Project> filterExactMatches(List<String> ids, List<Project> projects)
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
