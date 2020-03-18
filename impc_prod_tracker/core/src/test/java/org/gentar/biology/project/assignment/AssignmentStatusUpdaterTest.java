package org.gentar.biology.project.assignment;

import org.gentar.biology.gene.Gene;
import org.gentar.biology.intention.project_intention.ProjectIntention;
import org.gentar.biology.intention.project_intention_gene.ProjectIntentionGene;
import org.gentar.biology.mutation.molecular_type.MolecularMutationType;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectIntentionService;
import org.gentar.biology.project.ProjectQueryHelper;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusNames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssignmentStatusUpdaterTest
{
    @Mock
    private AssignmentStatusService assignmentStatusService;
    @Mock
    private ProjectIntentionService projectIntentionService;
    @Mock
    private ProjectQueryHelper projectQueryHelper;

    private AssignmentStatusUpdater testInstance;

    private static AssignmentStatus assigned =
        new AssignmentStatus(1L, AssignmentStatusNames.ASSIGNED_STATUS_NAME, "");
    private static AssignmentStatus inspect_gtl_mouse =
        new AssignmentStatus(1L, AssignmentStatusNames.INSPECT_GLT_MOUSE_STATUS_NAME, "");
    private static AssignmentStatus inspect_attempt =
        new AssignmentStatus(1L, AssignmentStatusNames.INSPECT_ATTEMPT_STATUS_NAME, "");
    private static AssignmentStatus inspect_conflict =
        new AssignmentStatus(1L, AssignmentStatusNames.INSPECT_CONFLICT_STATUS_NAME, "");
    private static AssignmentStatus conflict =
        new AssignmentStatus(1L, AssignmentStatusNames.CONFLICT_STATUS_NAME, "");

    @BeforeEach
    public void setup()
    {
        testInstance = new AssignmentStatusUpdater(
            assignmentStatusService, projectIntentionService, projectQueryHelper);
    }

    @Test
    public void testWhenNoIntentions()
    {
        Project project = new Project();
        when(
            assignmentStatusService.getAssignmentStatusByName(
                AssignmentStatusNames.ASSIGNED_STATUS_NAME))
            .thenReturn(assigned);

        AssignmentStatus newStatus = testInstance.checkConflicts(project);
        assertThat("Incorrect Assignment status:", newStatus.getName(), is("Assigned"));
    }

    @Test
    public void testWhenMoreThanOneGene()
    {
        Project project = new Project();

        List<ProjectIntentionGene> intentionGeneList = new ArrayList<>();
        intentionGeneList.add(buildProjectIntentionGene(1L));
        intentionGeneList.add(buildProjectIntentionGene(2L));
        when(projectQueryHelper.getIntentionGenesByProject(project)).thenReturn(intentionGeneList);
        when(
            assignmentStatusService.getAssignmentStatusByName(
                AssignmentStatusNames.ASSIGNED_STATUS_NAME))
            .thenReturn(assigned);

        AssignmentStatus newStatus = testInstance.checkConflicts(project);

        assertThat("Incorrect Assignment status:", newStatus.getName(), is("Assigned"));
        verify(projectIntentionService, times(0)).getProjectsWithSameGeneIntention(project);
    }

    @Test
    public void testWhenOneGeneDeletingAndNoMoreProjects()
    {
        Project project = new Project();

        List<ProjectIntentionGene> intentionGeneList = new ArrayList<>();
        intentionGeneList.add(buildCompleteProjectIntentionGene(1L, 1L, "Deletion", 1L));
        when(projectQueryHelper.getIntentionGenesByProject(project)).thenReturn(intentionGeneList);
        when(
            assignmentStatusService.getAssignmentStatusByName(
                AssignmentStatusNames.ASSIGNED_STATUS_NAME))
            .thenReturn(assigned);

        AssignmentStatus newStatus = testInstance.checkConflicts(project);

        assertThat("Incorrect Assignment status:", newStatus.getName(), is("Assigned"));
        verify(projectIntentionService, times(1)).getProjectsWithSameGeneIntention(project);
    }

    @Test
    public void testWhenOneGeneNotDeletingAndNoMoreProjects()
    {
        Project project = new Project();

        List<ProjectIntentionGene> intentionGeneList = new ArrayList<>();
        intentionGeneList.add(buildCompleteProjectIntentionGene(1L, 1L, "Point Mutation", 1L));
        when(projectQueryHelper.getIntentionGenesByProject(project)).thenReturn(intentionGeneList);
        when(
            assignmentStatusService.getAssignmentStatusByName(
                AssignmentStatusNames.ASSIGNED_STATUS_NAME))
            .thenReturn(assigned);

        AssignmentStatus newStatus = testInstance.checkConflicts(project);

        assertThat("Incorrect Assignment status:", newStatus.getName(), is("Assigned"));
        verify(projectIntentionService, times(0)).getProjectsWithSameGeneIntention(project);
    }

    @Test
    public void testWhenOneGeneDeletingAndMoreProjectsNoConflict()
    {
        Project project = new Project();

        List<ProjectIntentionGene> intentionGeneList = new ArrayList<>();
        intentionGeneList.add(buildCompleteProjectIntentionGene(1L, 1L, "Deletion", 1L));
        when(projectQueryHelper.getIntentionGenesByProject(project)).thenReturn(intentionGeneList);
        List<Project> otherProjects = new ArrayList<>();
        otherProjects.add(buildCompleteProjectIntentionGene(2L, 2L, "Point Mutation", 2L)
            .getProjectIntention().getProject());
        otherProjects.add(buildCompleteProjectIntentionGene(3L, 3L, "Point Mutation", 3L)
            .getProjectIntention().getProject());
        when(projectIntentionService.getProjectsWithSameGeneIntention(project)).thenReturn(otherProjects);
        when(
            assignmentStatusService.getAssignmentStatusByName(
                AssignmentStatusNames.ASSIGNED_STATUS_NAME))
            .thenReturn(assigned);

        AssignmentStatus newStatus = testInstance.checkConflicts(project);

        assertThat("Incorrect Assignment status:", newStatus.getName(), is("Assigned"));
        verify(projectIntentionService, times(1)).getProjectsWithSameGeneIntention(project);
    }

    @Test
    public void testWhenInspectGltMouse()
    {
        Project project = new Project();

        List<ProjectIntentionGene> intentionGeneList = new ArrayList<>();
        intentionGeneList.add(buildCompleteProjectIntentionGene(1L, 1L, "Deletion", 1L));
        when(projectQueryHelper.getIntentionGenesByProject(project)).thenReturn(intentionGeneList);
        List<Project> conflictingProjects = new ArrayList<>();

        // There is another project with same intention and same gene
        Project conflictingProject = buildCompleteProjectIntentionGene(2L, 2L, "Deletion", 1L)
            .getProjectIntention().getProject();
        Plan plan = buildPlanWithStatus(StatusNames.GENOTYPE_CONFIRMED);
        Set<Plan> plansForProject = new HashSet<>();
        plansForProject.add(plan);

        conflictingProject.setPlans(plansForProject);
        conflictingProjects.add(conflictingProject);
        when(projectIntentionService.getProjectsWithSameGeneIntention(project))
            .thenReturn(conflictingProjects);
        when(
            assignmentStatusService.getAssignmentStatusByName(
                AssignmentStatusNames.INSPECT_GLT_MOUSE_STATUS_NAME))
            .thenReturn(inspect_gtl_mouse);

        AssignmentStatus newStatus = testInstance.checkConflicts(project);

        assertThat("Incorrect Assignment status:", newStatus.getName(), is("Inspect - GLT Mouse"));
    }

    @Test
    public void testWhenInspectAttempt()
    {
        Project project = new Project();

        List<ProjectIntentionGene> intentionGeneList = new ArrayList<>();
        intentionGeneList.add(buildCompleteProjectIntentionGene(1L, 1L, "Deletion", 1L));
        when(projectQueryHelper.getIntentionGenesByProject(project)).thenReturn(intentionGeneList);
        List<Project> conflictingProjects = new ArrayList<>();

        // There is another project with same intention and same gene
        Project conflictingProject = buildCompleteProjectIntentionGene(2L, 2L, "Deletion", 1L)
            .getProjectIntention().getProject();
        Plan plan = buildPlanWithStatus(StatusNames.GENOTYPE_NOT_CONFIRMED);
        Set<Plan> plansForProject = new HashSet<>();
        plansForProject.add(plan);

        conflictingProject.setPlans(plansForProject);
        conflictingProjects.add(conflictingProject);
        when(projectIntentionService.getProjectsWithSameGeneIntention(project))
            .thenReturn(conflictingProjects);
        when(
            assignmentStatusService.getAssignmentStatusByName(
                AssignmentStatusNames.INSPECT_ATTEMPT_STATUS_NAME))
            .thenReturn(inspect_attempt);

        AssignmentStatus newStatus = testInstance.checkConflicts(project);

        assertThat("Incorrect Assignment status:", newStatus.getName(), is("Inspect - Attempt"));
    }

    @Test
    public void testWhenInspectConflict()
    {
        Project project = new Project();

        List<ProjectIntentionGene> intentionGeneList = new ArrayList<>();
        intentionGeneList.add(buildCompleteProjectIntentionGene(1L, 1L, "Deletion", 1L));
        when(projectQueryHelper.getIntentionGenesByProject(project)).thenReturn(intentionGeneList);
        List<Project> conflictingProjects = new ArrayList<>();

        // There is another project with same intention and same gene
        Project conflictingProject = buildCompleteProjectIntentionGene(2L, 2L, "Deletion", 1L)
            .getProjectIntention().getProject();
        conflictingProject.setAssignmentStatus(assigned);
        Plan plan = buildPlanWithStatus(StatusNames.ATTEMPT_ABORTED);
        Set<Plan> plansForProject = new HashSet<>();
        plansForProject.add(plan);

        conflictingProject.setPlans(plansForProject);
        conflictingProjects.add(conflictingProject);
        when(projectIntentionService.getProjectsWithSameGeneIntention(project))
            .thenReturn(conflictingProjects);
        when(
            assignmentStatusService.getAssignmentStatusByName(
                AssignmentStatusNames.INSPECT_CONFLICT_STATUS_NAME))
            .thenReturn(inspect_conflict);

        AssignmentStatus newStatus = testInstance.checkConflicts(project);

        assertThat("Incorrect Assignment status:", newStatus.getName(), is("Inspect - Conflict"));
    }

    @Test
    public void testWhenConflict()
    {
        Project project = new Project();

        List<ProjectIntentionGene> intentionGeneList = new ArrayList<>();
        intentionGeneList.add(buildCompleteProjectIntentionGene(1L, 1L, "Deletion", 1L));
        when(projectQueryHelper.getIntentionGenesByProject(project)).thenReturn(intentionGeneList);
        List<Project> conflictingProjects = new ArrayList<>();

        // There is another project with same intention and same gene
        Project conflictingProject = buildCompleteProjectIntentionGene(2L, 2L, "Deletion", 1L)
            .getProjectIntention().getProject();
        conflictingProject.setAssignmentStatus(inspect_gtl_mouse);
        Plan plan = buildPlanWithStatus(StatusNames.ATTEMPT_ABORTED);
        Set<Plan> plansForProject = new HashSet<>();
        plansForProject.add(plan);

        conflictingProject.setPlans(plansForProject);
        conflictingProjects.add(conflictingProject);
        when(projectIntentionService.getProjectsWithSameGeneIntention(project))
            .thenReturn(conflictingProjects);
        when(
            assignmentStatusService.getAssignmentStatusByName(
                AssignmentStatusNames.CONFLICT_STATUS_NAME))
            .thenReturn(conflict);

        AssignmentStatus newStatus = testInstance.checkConflicts(project);

        assertThat("Incorrect Assignment status:", newStatus.getName(), is("Conflict"));
    }

    private ProjectIntention buildProjectIntention(Long id, String molecularMutationTypeName)
    {
        ProjectIntention projectIntention = new ProjectIntention();
        projectIntention.setId(id);
        MolecularMutationType molecularMutationType = new MolecularMutationType();
        molecularMutationType.setName(molecularMutationTypeName);
        projectIntention.setMolecularMutationType(molecularMutationType);
        return projectIntention;
    }

    private ProjectIntentionGene buildProjectIntentionGene(Long geneId)
    {
        ProjectIntentionGene intentionGene = new ProjectIntentionGene();
        intentionGene.setGene(buildGeneWithId(geneId));
        return intentionGene;
    }

    private ProjectIntentionGene buildCompleteProjectIntentionGene(
        Long projectId, Long intentionId, String molecularMutationTypeName, Long geneId)
    {
        Project project = new Project();
        project.setId(projectId);
        ProjectIntention projectIntention = buildProjectIntention(intentionId, molecularMutationTypeName);
        projectIntention.setProject(project);
        project.setProjectIntentions(Arrays.asList(projectIntention));

        ProjectIntentionGene projectIntentionGene = buildProjectIntentionGene(geneId);
        projectIntentionGene.setProjectIntention(projectIntention);
        projectIntention.setProjectIntentionGene(projectIntentionGene);

        return projectIntentionGene;
    }

    private Gene buildGeneWithId(Long id)
    {
        Gene gene = new Gene();
        gene.setId(id);
        gene.setAccId("AccID:" + id);
        gene.setSymbol("Symbol:" + id);
        return gene;
    }

    private Plan buildPlanWithStatus(String statusName)
    {
        Plan plan = new Plan();
        Status status = new Status();
        status.setName(statusName);
        plan.setStatus(status);
        return plan;
    }

}