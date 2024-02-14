/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 * <p>
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package org.gentar.biology.project.engine;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.PlanCreator;
import org.gentar.biology.project.assignment.AssignmentStatusUpdater;
import org.springframework.stereotype.Component;
import org.gentar.biology.project.Project;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.Set;

/**
 * Class with the logic to save a project in the system.
 */
@Component
@Transactional
public class ProjectCreator
{
    @PersistenceContext
    private final EntityManager entityManager;

    private final AssignmentStatusUpdater assignmentStatusUpdater;
    private final HistoryService<Project> historyService;
    private final ProjectValidator projectValidator;
    private final PlanCreator planCreator;

    public ProjectCreator(
        EntityManager entityManager,
        AssignmentStatusUpdater assignmentStatusUpdater,
        HistoryService<Project> historyService,
        ProjectValidator projectValidator,
        PlanCreator planCreator)
    {
        this.entityManager = entityManager;
        this.assignmentStatusUpdater = assignmentStatusUpdater;
        this.historyService = historyService;
        this.projectValidator = projectValidator;
        this.planCreator = planCreator;
    }

    /**
     * Create a project in the system and keep trace of the event in the history.
     * @param project Object with the project information before it is saved in database.
     * @return Created project.
     */
    public Project createProject(Project project)
    {
        validateData(project);
        validatePermissionToCreate(project);
        setInitialAssignmentStatus(project);
        Project createdProject = saveProject(project);
        savePlans(createdProject);
        registerCreationInHistory(project);
        return createdProject;
    }

    private void savePlans(Project createdProject)
    {
        Set<Plan> plans = createdProject.getPlans();
        if (plans != null)
        {
            plans.forEach(planCreator::createPlan);
        }
    }

    private void validateData(Project project)
    {
        projectValidator.validateData(project);
    }

    private void validatePermissionToCreate(Project project)
    {
        projectValidator.validatePermissionToCreateProject(project);
    }

    private Project saveProject(Project project)
    {
        entityManager.persist(project);
        project.setTpn(buildTpn(project.getId()));
        return project;
    }

    private void registerCreationInHistory(Project project)
    {
        History history = historyService.buildCreationTrack(project, project.getId());
        historyService.saveTrackOfChanges(history);
    }

    private String buildTpn(Long id)
    {
        String identifier = String.format("%0" + 9 + "d", id);
        identifier = "TPN:" + identifier;
        return identifier;
    }

    private void setInitialAssignmentStatus(Project project)
    {
        assignmentStatusUpdater.recalculateConflicts(project);
    }

}
