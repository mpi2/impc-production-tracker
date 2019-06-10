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
import uk.ac.ebi.impc_prod_tracker.data.biology.intented_mouse_gene.IntendedMouseGene;
import uk.ac.ebi.impc_prod_tracker.data.biology.intented_mouse_gene.IntendedMouseGeneRepository;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.ProjectRepository;
import java.util.List;

@Component
public class ProjectServiceImpl implements ProjectService
{
    private ProjectRepository projectRepository;
    private IntendedMouseGeneRepository intendedMouseGeneRepository;

    ProjectServiceImpl(
        ProjectRepository projectRepository,
        IntendedMouseGeneRepository intendedMouseGeneRepository)
    {
        this.projectRepository = projectRepository;
        this.intendedMouseGeneRepository = intendedMouseGeneRepository;
    }

    @Override
    public List<Project> getProjects()
    {
        return projectRepository.findAll();
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

    public Page<Project> getProjectsByMarkerSymbols(List<String> markerSymbols, Pageable pageable)
    {
        List<IntendedMouseGene> intendedMouseGenes =
            intendedMouseGeneRepository.findAllBySymbolIn(markerSymbols);
        Page<Project> projects =
            projectRepository.findAllByIntendedMouseGenesIn(intendedMouseGenes, pageable);
        return projects;

    }
}
