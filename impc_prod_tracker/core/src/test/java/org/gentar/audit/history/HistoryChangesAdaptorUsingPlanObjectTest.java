package org.gentar.audit.history;

import org.gentar.audit.history.ChangeDescription;
import org.gentar.audit.history.HistoryChangesAdaptor;
import org.hamcrest.Matchers;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;
import org.gentar.biology.plan.attempt.crispr.genotype_primer.GenotypePrimer;
import org.gentar.biology.plan.attempt.crispr.guide.Guide;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.project.privacy.Privacy;
import org.gentar.biology.project.Project;
import org.gentar.organization.work_unit.WorkUnit;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Class to test {@link HistoryChangesAdaptor} using Plan objects as example.
 */
public class HistoryChangesAdaptorUsingPlanObjectTest
{
    @Test
    public void testWhenNoChanges()
    {
        Plan originalPlan = buildBasicPlan();
        Plan newPlan = buildBasicPlan();
        HistoryChangesAdaptor<Plan> historyChangesAdaptor =
            new HistoryChangesAdaptor<>(Arrays.asList(), originalPlan, newPlan);

        List<ChangeDescription> changeDescriptionList =  historyChangesAdaptor.getChanges();
        assertThat("No changes where expected", changeDescriptionList.isEmpty(), is(true));
    }

    @Test
    public void testWhenCommentChanged()
    {
        Plan originalPlan = buildBasicPlan();
        Plan newPlan = buildBasicPlan();
        newPlan.setComment("a new comment");
        HistoryChangesAdaptor<Plan> historyChangesAdaptor =
            new HistoryChangesAdaptor<>(Arrays.asList(), originalPlan, newPlan);

        List<ChangeDescription> changeDescriptionList =  historyChangesAdaptor.getChanges();

        assertThat("Only one change is expected:", changeDescriptionList.size(), is(1));
        ChangeDescription change = changeDescriptionList.get(0);
        assertThat("No expected property", change.getProperty(), is("comment"));
        assertThat("No expected old value", change.getOldValue(), is(nullValue()));
        assertThat("No expected new value", change.getNewValue(), is("a new comment"));
    }

    @Test
    public void testWhenPrivacyChanged()
    {
        Project originalProject = buildBasicProject();
        Project newProject = buildBasicProject();
        Privacy newPrivacy = new Privacy();
        newPrivacy.setId(2L);
        newPrivacy.setName("New Privacy");
        newProject.setPrivacy(newPrivacy);

        HistoryChangesAdaptor<Project> historyChangesAdaptor =
            new HistoryChangesAdaptor<>(Arrays.asList(), originalProject, newProject);

        List<ChangeDescription> changeDescriptionList =  historyChangesAdaptor.getChanges();
        System.out.println(changeDescriptionList);

        assertThat("Only one change is expected:", changeDescriptionList.size(), is(1));
        ChangeDescription change = changeDescriptionList.get(0);
        assertThat("No expected property", change.getProperty(), is("privacy.name"));
        assertThat("No expected old value", change.getOldValue(), is("Privacy1"));
        assertThat("No expected new value", change.getNewValue(), is("New Privacy"));
    }

    @Test
    public void testWhenCommentAndPrivacyChanged()
    {
        Project originalProject = buildBasicProject();
        Project newProject = buildBasicProject();
        Privacy newPrivacy = new Privacy();
        newPrivacy.setId(2L);
        newPrivacy.setName("New Privacy");
        newProject.setPrivacy(newPrivacy);
        newProject.setComment("A comment");
        HistoryChangesAdaptor<Project> historyChangesAdaptor =
            new HistoryChangesAdaptor<>(Arrays.asList(), originalProject, newProject);

        List<ChangeDescription> changeDescriptionList =  historyChangesAdaptor.getChanges();
        System.out.println(changeDescriptionList);

        assertThat("Unexpected number of changes:", changeDescriptionList.size(), is(2));
        ChangeDescription change1 = getChange("comment", changeDescriptionList);
        assertThat("Change not found", change1, is(notNullValue()));
        assertThat("No expected old value", change1.getOldValue(), is(nullValue()));
        assertThat("No expected old value", change1.getNewValue(), is("A comment"));
        ChangeDescription change2 = getChange("privacy.name", changeDescriptionList);
        assertThat("Change not found", change2, is(notNullValue()));
        assertThat("No expected old value", change2.getOldValue(), is("Privacy1"));
        assertThat("No expected old value", change2.getNewValue(), is("New Privacy"));
    }

    @Test
    public void testWhenNestedElementDataChanges()
    {
        Plan originalPlan = buildBasicPlan();
        Plan newPlan = buildBasicPlan();
        HistoryChangesAdaptor<Plan> historyChangesAdaptor =
            new HistoryChangesAdaptor<>(Arrays.asList(), originalPlan, newPlan);

        CrisprAttempt crisprAttempt = new CrisprAttempt();
        crisprAttempt.setId(1L);
        crisprAttempt.setComment("a");
        originalPlan.setCrisprAttempt(crisprAttempt);

        CrisprAttempt crisprAttempt1Modified = new CrisprAttempt();
        crisprAttempt1Modified.setId(1L);
        crisprAttempt1Modified.setComment("b");
        newPlan.setCrisprAttempt(crisprAttempt1Modified);

        List<ChangeDescription> changeDescriptionList = historyChangesAdaptor.getChanges();
        System.out.println(changeDescriptionList);
        assertThat("Unexpected number of changes:", changeDescriptionList.size(), is(1));

    }

    @Test
    public void testWhenComparingNullGuidesWithEmptyGuides()
    {
        Plan originalPlan = buildBasicPlan();
        Plan newPlan = buildBasicPlan();
        HistoryChangesAdaptor<Plan> historyChangesAdaptor =
            new HistoryChangesAdaptor<>(Arrays.asList(), originalPlan, newPlan);

        CrisprAttempt crisprAttempt1 = new CrisprAttempt();
        crisprAttempt1.setGuides(null);
        originalPlan.setCrisprAttempt(crisprAttempt1);

        CrisprAttempt crisprAttempt2 = new CrisprAttempt();
        crisprAttempt2.setGuides(new HashSet<>());
        newPlan.setCrisprAttempt(crisprAttempt2);

        List<ChangeDescription> changeDescriptionList = historyChangesAdaptor.getChanges();
        System.out.println(changeDescriptionList);
        assertThat(
            "List of changes should be empty [" + changeDescriptionList + "]",
            changeDescriptionList.isEmpty(), is(true));
    }

    @Test
    public void testWhenAddedGuides()
    {
        Plan originalPlan = buildBasicPlan();
        Plan newPlan = buildBasicPlan();
        HistoryChangesAdaptor<Plan> historyChangesAdaptor =
            new HistoryChangesAdaptor<>(Arrays.asList(), originalPlan, newPlan);

        CrisprAttempt crisprAttempt1 = new CrisprAttempt();
        crisprAttempt1.setGuides(null);
        originalPlan.setCrisprAttempt(crisprAttempt1);

        CrisprAttempt crisprAttempt2 = new CrisprAttempt();
        Set<Guide> guides = new HashSet<>();
        Guide guide1 = new Guide();
        guide1.setId(1L);
        guide1.setChr("X");
        guide1.setSequence("GCCTCAATCTGCACAGTATTGGG");
        guide1.setStart(105880383);
        guide1.setStop(105880405);
        guide1.setTruncatedGuide(false);
        guide1.setGrnaConcentration(null);

        Guide guide2 = new Guide();
        guide2.setId(2L);
        guide2.setChr("X");
        guide2.setSequence("AAATCAATCTGCACAGTATTGGG");
        guide2.setStart(9999999);
        guide2.setStop(999999999);
        guide2.setTruncatedGuide(false);
        guide2.setGrnaConcentration(null);
        crisprAttempt1.setGuides(null);
        guides.add(guide1);
        guides.add(guide2);
        crisprAttempt2.setGuides(guides);
        newPlan.setCrisprAttempt(crisprAttempt2);
        List<ChangeDescription> changeDescriptionList = historyChangesAdaptor.getChanges();
        System.out.println(changeDescriptionList);
        assertThat("Unexpected number of changes:", changeDescriptionList.size(), is(2));
        System.out.println(changeDescriptionList);

        ChangeDescription changeDescriptionElement1 = getChange("crisprAttempt.guides#1", changeDescriptionList);
        assertThat("Unexpected property:", changeDescriptionElement1.getProperty(), is("crisprAttempt.guides#1"));
        assertThat("Unexpected old value:", changeDescriptionElement1.getOldValue(), is(nullValue()));
        assertThat("Unexpected new value:", changeDescriptionElement1.getNewValue(), Matchers.is(guide1));

        ChangeDescription changeDescriptionElement2 = getChange("crisprAttempt.guides#2", changeDescriptionList);
        assertThat("Unexpected property:", changeDescriptionElement2.getProperty(), is("crisprAttempt.guides#2"));
        assertThat("Unexpected old value:", changeDescriptionElement2.getOldValue(), is(nullValue()));
        assertThat("Unexpected new value:", changeDescriptionElement2.getNewValue(), Matchers.is(guide2));
    }

    @Test
    public void testWhenGuidesDifferent()
    {
        Plan originalPlan = buildBasicPlan();
        Plan newPlan = buildBasicPlan();
        HistoryChangesAdaptor<Plan> historyChangesAdaptor =
            new HistoryChangesAdaptor<>(Arrays.asList(), originalPlan, newPlan);

        CrisprAttempt crisprAttempt1 = new CrisprAttempt();
        crisprAttempt1.setGuides(null);
        originalPlan.setCrisprAttempt(crisprAttempt1);

        CrisprAttempt crisprAttempt2 = new CrisprAttempt();
        Set<Guide> guides = new HashSet<>();
        Guide guide1 = new Guide();
        guide1.setId(1L);
        guide1.setChr("X");
        guide1.setSequence("GCCTCAATCTGCACAGTATTGGG");
        guide1.setStart(105880383);
        guide1.setStop(105880405);
        guide1.setTruncatedGuide(false);
        guide1.setGrnaConcentration(null);

        Guide guide2 = new Guide();
        guide2.setId(2L);
        guide2.setChr("X");
        guide2.setSequence("GCCTCAATCTGCACAGTATTGGG");
        guide2.setStart(105880383);
        guide2.setStop(105880405);
        guide2.setTruncatedGuide(false);
        guide2.setGrnaConcentration(null);
        crisprAttempt1.setGuides(null);
        guides.add(guide1);
        guides.add(guide2);
        crisprAttempt2.setGuides(new HashSet<>());
        newPlan.setCrisprAttempt(crisprAttempt2);

        List<ChangeDescription> changeDescriptionList =  historyChangesAdaptor.getChanges();
        System.out.println(changeDescriptionList);
    }

    @Test
    public void testWhenAddedPrimer()
    {
        Plan originalPlan = buildBasicPlan();
        Plan newPlan = buildBasicPlan();
        HistoryChangesAdaptor<Plan> historyChangesAdaptor =
            new HistoryChangesAdaptor<>(Arrays.asList(), originalPlan, newPlan);

        CrisprAttempt crisprAttempt1 = new CrisprAttempt();
        originalPlan.setCrisprAttempt(crisprAttempt1);

        CrisprAttempt crisprAttempt2 = new CrisprAttempt();
        GenotypePrimer primer1 = new GenotypePrimer();
        primer1.setSequence("ACACCCCTAGTCTTGTGTCTCA");
        primer1.setName("Dnah10 KO F");
        Set<GenotypePrimer> genotypePrimers = new HashSet<>();
        genotypePrimers.add(primer1);
        crisprAttempt2.setPrimers(genotypePrimers);
        newPlan.setCrisprAttempt(crisprAttempt2);

        List<ChangeDescription> changeDescriptionList = historyChangesAdaptor.getChanges();
        System.out.println(changeDescriptionList);
        assertThat("Unexpected number of changes:", changeDescriptionList.size(), is(1));
    }

    private Plan buildBasicPlan()
    {
        Plan plan = new Plan();
        Privacy privacy = new Privacy();
        privacy.setId(1L);
        privacy.setName("Privacy1");
        WorkUnit workUnit = new WorkUnit("WU1");
        workUnit.setId(1L);
        plan.setWorkUnit(workUnit);

        return plan;
    }

    private Project buildBasicProject()
    {
        Project project = new Project();
        Privacy privacy = new Privacy();
        privacy.setId(1L);
        privacy.setName("Privacy1");
        project.setPrivacy(privacy);

        return project;
    }

    private ChangeDescription getChange(
        String propertyName, List<ChangeDescription> changeDescriptionList)
    {
        Optional<ChangeDescription> changeDescriptionOptional =
            changeDescriptionList.stream().filter(x -> x.getProperty().equals(propertyName)).findFirst();
        return changeDescriptionOptional.orElse(null);
    }
}