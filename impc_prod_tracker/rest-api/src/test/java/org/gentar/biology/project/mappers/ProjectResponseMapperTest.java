package org.gentar.biology.project.mappers;

import org.gentar.biology.intention.ProjectIntentionMapper;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectResponseDTO;
import org.gentar.biology.project.assignment.AssignmentStatus;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusStampMapper;
import org.gentar.organization.work_group.WorkGroup;
import org.gentar.organization.work_unit.WorkUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProjectResponseMapperTest
{
    public static final String ASSIGNMENT_STATUS = "assignmentStatus";
    public static final String PROJECT_COMMENT = "projectComment";
    public static final String SUMMARY_STATUS = "summaryStatus";
    public static final String TPN = "tpn";
    public static final String WORK_UNIT_NAME = "workUnitName";
    public static final String WORK_GROUP_NAME = "workGroupName";
    private ProjectResponseMapper testInstance;

    @Mock
    private ProjectCommonDataMapper projectCommonDataMapper;
    @Mock
    private StatusStampMapper statusStampMapper;
    @Mock
    private ProjectIntentionMapper projectIntentionMapper;

    @BeforeEach
    void setUp()
    {
        testInstance = new ProjectResponseMapper(
            projectCommonDataMapper, statusStampMapper, projectIntentionMapper);
    }

    @Test
    void toDto()
    {
        Project project = buildProject();
        ProjectResponseDTO projectResponseDTO = testInstance.toDto(project);

        verify(projectCommonDataMapper, times(1)).toDto(project);
        verify(statusStampMapper, times(1)).toDtos(project.getAssignmentStatusStamps());
        verify(projectIntentionMapper, times(1)).toDtos(project.getProjectIntentions());
        assertThat(projectResponseDTO.getTpn(), is(TPN));
        assertThat(projectResponseDTO.getAssignmentStatusName(), is(ASSIGNMENT_STATUS));
        assertThat(projectResponseDTO.getSummaryStatusName(), is(SUMMARY_STATUS));
        assertThat(projectResponseDTO.getRelatedWorkUnitNames().size(), is(1));
        assertThat
            (projectResponseDTO.getRelatedWorkUnitNames().iterator().next(), is(WORK_UNIT_NAME));
        assertThat(projectResponseDTO.getRelatedWorkGroupNames().size(), is(1));
        assertThat(
            projectResponseDTO.getRelatedWorkGroupNames().iterator().next(), is(WORK_GROUP_NAME));
    }

    private Project buildProject()
    {
        Project project = new Project();
        project.setTpn(TPN);
        AssignmentStatus assignmentStatus = new AssignmentStatus();
        assignmentStatus.setName(ASSIGNMENT_STATUS);
        project.setAssignmentStatus(assignmentStatus);
        project.setComment(PROJECT_COMMENT);
        Status summaryStatus = new Status();
        summaryStatus.setName(SUMMARY_STATUS);
        project.setSummaryStatus(summaryStatus);
        Plan plan = new Plan();
        WorkUnit workUnit = new WorkUnit();
        workUnit.setName(WORK_UNIT_NAME);
        plan.setWorkUnit(workUnit);
        WorkGroup workGroup = new WorkGroup();
        workGroup.setName(WORK_GROUP_NAME);
        plan.setWorkGroup(workGroup);
        Set<Plan> plans = new HashSet<>();
        plans.add(plan);
        project.setPlans(plans);
        return project;
    }
}
