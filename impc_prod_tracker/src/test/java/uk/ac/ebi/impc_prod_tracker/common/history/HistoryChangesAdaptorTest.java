package uk.ac.ebi.impc_prod_tracker.common.history;

import org.junit.Test;
import org.gentar.biology.assignment_status.AssignmentStatus;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.project.Project;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HistoryChangesAdaptorTest
{
    private HistoryChangesAdaptor<Project> testInstance;

    @Test
    public void test()
    {
        Plan plan1 = new Plan();
        plan1.setId(10L);
        plan1.setPin("pin1");
        Plan plan2 = new Plan();
        plan2.setId(20L);
        plan2.setPin("pin2");

        Set planSet1 =  new HashSet();
        planSet1.add(plan1);
        Set planSet2 =  new HashSet();
        planSet2.add(plan2);

        AssignmentStatus assignmentStatus1 = new AssignmentStatus();
        assignmentStatus1.setName("S1");
        assignmentStatus1.setId(1l);

        AssignmentStatus assignmentStatus2 = new AssignmentStatus();
        assignmentStatus2.setName("S2");
        assignmentStatus2.setId(2l);

        Project p1 = new Project();
        p1.setId(1L);
        p1.setTpn("tpn1");
        p1.setPlans(planSet1);
        Project p2 = new Project();
        p2.setId(2L);
        p2.setTpn("tpn2");
        p2.setPlans(planSet2);

        p1.setAssignmentStatus(assignmentStatus1);
        p2.setAssignmentStatus(assignmentStatus2);
        testInstance = new HistoryChangesAdaptor<>(Arrays.asList(), p1, p2);

        List<ChangeDescription> changeDescriptionList = testInstance.getChanges();
        System.out.println(changeDescriptionList);

        assertThat("", changeDescriptionList.size(), is(4));
    }
}