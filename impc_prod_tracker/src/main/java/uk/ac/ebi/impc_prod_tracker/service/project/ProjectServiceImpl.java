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
package uk.ac.ebi.impc_prod_tracker.service.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.ProjectRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectServiceImpl implements ProjectService
{
    private ProjectRepository projectRepository;

    ProjectServiceImpl(ProjectRepository projectRepository)
    {
        this.projectRepository = projectRepository;
    }

    @Override
    public List<Project> getProjects()
    {
        List<Project> listResult = new ArrayList<>();
        Iterable<Project> result = projectRepository.findAll();
        result.forEach(listResult::add);

        return listResult;
    }

    @Override
    public Project getProjectByTpn(String tpn)
    {
        Project project = projectRepository.findProjectByTpn(tpn);
        return project;
    }

    @Override
    public Page<Project> getPaginatedProjects(Pageable pageable) {
        Page<Project> projects = projectRepository.findAll(pageable);
        return projects;
    }
}
