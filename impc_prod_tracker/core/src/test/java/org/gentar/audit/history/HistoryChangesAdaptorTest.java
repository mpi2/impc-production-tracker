package org.gentar.audit.history;

import org.gentar.audit.diff.ChangeType;

import org.gentar.biology.project.assignment.AssignmentStatus;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.project.Project;
import org.gentar.biology.species.Species;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class HistoryChangesAdaptorTest
{
    private HistoryChangesAdaptor<Project> testInstance;

    @Test
    public void testSmallClass()
    {
        Species species1 = new Species();
        species1.setId(1L);
        Project project1 = new Project();
        project1.setId(1L);
        project1.setSpecies(new HashSet<>(List.of(species1)));
        species1.setProjects(new HashSet<>(List.of(project1)));

        Species species2 = new Species();
        species2.setId(2L);
        Project project2 = new Project();
        project2.setId(2L);
        project2.setSpecies(new HashSet<>(List.of(species2)));
        species2.setProjects(new HashSet<>(List.of(project2)));

        HistoryChangesAdaptor<Project> historyChangesAdaptor =
            new HistoryChangesAdaptor<>(List.of("id"), project1, project2);

        List<ChangeDescription> changeDescriptionList = historyChangesAdaptor.getChanges();
    }

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
        assignmentStatus1.setId(1L);

        AssignmentStatus assignmentStatus2 = new AssignmentStatus();
        assignmentStatus2.setName("S2");
        assignmentStatus2.setId(2L);

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
        testInstance = new HistoryChangesAdaptor<>(List.of("id"), p1, p2);

        List<ChangeDescription> changeDescriptionList = testInstance.getChanges();

        assertThat("", changeDescriptionList.size(), is(6));

        ChangeDescription changeDescription1 =
            getChangeDescription("assignmentStatus.name", changeDescriptionList);
        assertThat(changeDescription1.getOldValue(), is("S1"));
        assertThat(changeDescription1.getNewValue(), is("S2"));
        assertThat(changeDescription1.getChangeType(), is(ChangeType.CHANGED_FIELD));

        ChangeDescription changeDescription2 =
            getChangeDescription("plans.[10]", changeDescriptionList);
        assertThat(changeDescription2.getOldValue(), is(plan1));
        assertThat(changeDescription2.getNewValue(), is(nullValue()));
        assertThat(changeDescription2.getChangeType(), is(ChangeType.REMOVED));

        ChangeDescription changeDescription3 =
            getChangeDescription("plans.[10].pin", changeDescriptionList);
        assertThat(changeDescription3.getOldValue(), is("pin1"));
        assertThat(changeDescription3.getNewValue(), is(nullValue()));
        assertThat(changeDescription3.getChangeType(), is(ChangeType.CHANGED_FIELD));

        ChangeDescription changeDescription4 =
            getChangeDescription("plans.[20]", changeDescriptionList);
        assertThat(changeDescription4.getOldValue(), is(nullValue()));
        assertThat(changeDescription4.getNewValue(), is(plan2));
        assertThat(changeDescription4.getChangeType(), is(ChangeType.ADDED));

        ChangeDescription changeDescription5 =
            getChangeDescription("plans.[20].pin", changeDescriptionList);
        assertThat(changeDescription5.getOldValue(), is(nullValue()));
        assertThat(changeDescription5.getNewValue(), is("pin2"));
        assertThat(changeDescription5.getChangeType(), is(ChangeType.CHANGED_FIELD));

        ChangeDescription changeDescription6 =
            getChangeDescription("tpn", changeDescriptionList);
        assertThat(changeDescription6.getOldValue(), is("tpn1"));
        assertThat(changeDescription6.getNewValue(), is("tpn2"));
        assertThat(changeDescription6.getChangeType(), is(ChangeType.CHANGED_FIELD));
    }

    private ChangeDescription getChangeDescription(
        String field, List<ChangeDescription> changeDescriptions)
    {
        Optional<ChangeDescription> changeDescription =
            changeDescriptions.stream().filter(x -> x.getProperty().equals(field)).findFirst();
        return changeDescription.orElse(null);
    }
}