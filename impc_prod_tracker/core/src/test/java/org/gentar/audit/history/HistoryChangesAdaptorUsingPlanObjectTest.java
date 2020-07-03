package org.gentar.audit.history;

import org.gentar.biology.plan.type.PlanType;
import org.gentar.util.CollectionPrinter;
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
    private HistoryChangesAdaptor<Plan> historyChangesAdaptor;

    @Test
    public void testWhenNoChanges()
    {
        Plan originalPlan = buildBasicPlan();
        Plan newPlan = buildBasicPlan();
        historyChangesAdaptor = new HistoryChangesAdaptor<>(originalPlan, newPlan);

        List<ChangeDescription> changeDescriptionList = historyChangesAdaptor.getChanges();

        assertThat("No changes where expected", changeDescriptionList.isEmpty(), is(true));
    }

    @Test
    public void testWhenCommentChanged()
    {
        Plan originalPlan = buildBasicPlan();
        Plan newPlan = buildBasicPlan();
        newPlan.setComment("a new comment");
        historyChangesAdaptor = new HistoryChangesAdaptor<>(originalPlan, newPlan);

        List<ChangeDescription> changeDescriptionList =  historyChangesAdaptor.getChanges();

        assertThat("Only one change is expected:", changeDescriptionList.size(), is(1));
        ChangeDescription change = changeDescriptionList.get(0);
        assertThat("No expected property", change.getProperty(), is("comment"));
        assertThat("No expected old value", change.getOldValue(), is(nullValue()));
        assertThat("No expected new value", change.getNewValue(), is("a new comment"));
    }

    @Test
    public void testWhenPlanTypeChanged()
    {
        Plan originalPlan = buildBasicPlan();
        PlanType originalPlanType = new PlanType();
        originalPlanType.setId(1L);
        originalPlanType.setName("originalPlanTypeName");
        originalPlan.setPlanType(originalPlanType);

        Plan newPlan = buildBasicPlan();
        PlanType newPlanType = new PlanType();
        newPlanType.setId(2L);
        newPlanType.setName("newPlanType");
        newPlan.setPlanType(newPlanType);
        historyChangesAdaptor = new HistoryChangesAdaptor<>(originalPlan, newPlan);

        List<ChangeDescription> changeDescriptionList = historyChangesAdaptor.getChanges();

        assertThat("Only one change is expected:", changeDescriptionList.size(), is(2));
        ChangeDescription change1 = getChange("planType.id", changeDescriptionList);
        assertThat("No expected property", change1.getProperty(), is("planType.id"));
        assertThat("No expected old value", change1.getOldValue(), is(1L));
        assertThat("No expected new value", change1.getNewValue(), is(2L));

        ChangeDescription change2 = getChange("planType.name", changeDescriptionList);
        assertThat("No expected property", change2.getProperty(), is("planType.name"));
        assertThat("No expected old value", change2.getOldValue(), is("originalPlanTypeName"));
        assertThat("No expected new value", change2.getNewValue(), is("newPlanType"));
    }

    @Test
    public void testWhenCommentAndPlanTypeChanged()
    {
        Plan originalPlan = buildBasicPlan();
        PlanType originalPlanType = new PlanType();
        originalPlanType.setId(1L);
        originalPlanType.setName("originalPlanTypeName");
        originalPlan.setPlanType(originalPlanType);

        Plan newPlan = buildBasicPlan();
        PlanType newPlanType = new PlanType();
        newPlanType.setId(2L);
        newPlanType.setName("newPlanType");
        newPlan.setPlanType(newPlanType);
        newPlan.setComment("New comment");

        historyChangesAdaptor = new HistoryChangesAdaptor<>(originalPlan, newPlan);
        List<ChangeDescription> changeDescriptionList = historyChangesAdaptor.getChanges();

        assertThat("Unexpected number of changes:", changeDescriptionList.size(), is(3));
        ChangeDescription change1 = getChange("comment", changeDescriptionList);
        assertThat("Change not found", change1, is(notNullValue()));
        assertThat("No expected old value", change1.getOldValue(), is(nullValue()));
        assertThat("No expected old value", change1.getNewValue(), is("New comment"));

        ChangeDescription change2 = getChange("planType.id", changeDescriptionList);
        assertThat("Change not found", change2, is(notNullValue()));
        assertThat("No expected old value", change2.getOldValue(), is(1L));
        assertThat("No expected old value", change2.getNewValue(), is(2L));

        ChangeDescription change3 = getChange("planType.name", changeDescriptionList);
        assertThat("Change not found", change3, is(notNullValue()));
        assertThat("No expected old value", change3.getOldValue(), is("originalPlanTypeName"));
        assertThat("No expected old value", change3.getNewValue(), is("newPlanType"));
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
        assertThat("Unexpected number of changes:", changeDescriptionList.size(), is(1));
        ChangeDescription change = getChange("crisprAttempt.comment", changeDescriptionList);
        assertThat("Change not found", change, is(notNullValue()));
        assertThat("No expected old value", change.getOldValue(), is("a"));
        assertThat("No expected old value", change.getNewValue(), is("b"));
    }

    @Test
    public void testWhenComparingNullGuidesWithEmptyGuides()
    {
        Plan originalPlan = buildBasicPlan();
        Plan newPlan = buildBasicPlan();
        historyChangesAdaptor = new HistoryChangesAdaptor<>(originalPlan, newPlan);

        CrisprAttempt crisprAttempt1 = new CrisprAttempt();
        crisprAttempt1.setGuides(null);
        originalPlan.setCrisprAttempt(crisprAttempt1);

        CrisprAttempt crisprAttempt2 = new CrisprAttempt();
        crisprAttempt2.setGuides(new HashSet<>());
        newPlan.setCrisprAttempt(crisprAttempt2);

        List<ChangeDescription> changeDescriptionList = historyChangesAdaptor.getChanges();
        assertThat(
            "List of changes should be empty [" + changeDescriptionList + "]",
            changeDescriptionList.isEmpty(), is(true));
    }

    @Test
    public void testWhenAddedGuides()
    {
        Plan originalPlan = buildBasicPlan();
        Plan newPlan = buildBasicPlan();
        historyChangesAdaptor = new HistoryChangesAdaptor<>(originalPlan, newPlan);

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
        assertThat("Unexpected number of changes:", changeDescriptionList.size(), is(14));

        ChangeDescription changeDescription1 =
            getChange("crisprAttempt.guides.[1]", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription1.getProperty(), is("crisprAttempt.guides.[1]"));
        assertThat(
            "Unexpected old value:", changeDescription1.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription1.getNewValue(), Matchers.is(guide1));

        ChangeDescription changeDescription2 =
            getChange("crisprAttempt.guides.[1].chr", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription2.getProperty(), is("crisprAttempt.guides.[1].chr"));
        assertThat(
            "Unexpected old value:", changeDescription2.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription2.getNewValue(), Matchers.is("X"));

        ChangeDescription changeDescription3 =
            getChange("crisprAttempt.guides.[1].id", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription3.getProperty(), is("crisprAttempt.guides.[1].id"));
        assertThat(
            "Unexpected old value:", changeDescription3.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription3.getNewValue(), Matchers.is(1L));

        ChangeDescription changeDescription4 =
            getChange("crisprAttempt.guides.[1].sequence", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription4.getProperty(), is("crisprAttempt.guides.[1].sequence"));
        assertThat(
            "Unexpected old value:", changeDescription4.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription4.getNewValue(), Matchers.is("GCCTCAATCTGCACAGTATTGGG"));

        ChangeDescription changeDescription5 =
            getChange("crisprAttempt.guides.[1].start", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription5.getProperty(), is("crisprAttempt.guides.[1].start"));
        assertThat(
            "Unexpected old value:", changeDescription5.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription5.getNewValue(), Matchers.is(105880383));

        ChangeDescription changeDescription6 =
            getChange("crisprAttempt.guides.[1].stop", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription6.getProperty(), is("crisprAttempt.guides.[1].stop"));
        assertThat(
            "Unexpected old value:", changeDescription6.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription6.getNewValue(), Matchers.is(105880405));

        ChangeDescription changeDescription7 =
            getChange("crisprAttempt.guides.[1].truncatedGuide", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription7.getProperty(), is("crisprAttempt.guides.[1].truncatedGuide"));
        assertThat(
            "Unexpected old value:", changeDescription7.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription7.getNewValue(), Matchers.is(false));

        ChangeDescription changeDescription8 =
            getChange("crisprAttempt.guides.[2]", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription8.getProperty(), is("crisprAttempt.guides.[2]"));
        assertThat(
            "Unexpected old value:", changeDescription8.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription8.getNewValue(), Matchers.is(guide2));

        ChangeDescription changeDescription9 =
            getChange("crisprAttempt.guides.[2].chr", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription9.getProperty(), is("crisprAttempt.guides.[2].chr"));
        assertThat(
            "Unexpected old value:", changeDescription9.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription9.getNewValue(), Matchers.is("X"));

        ChangeDescription changeDescription10 =
            getChange("crisprAttempt.guides.[2].id", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription10.getProperty(), is("crisprAttempt.guides.[2].id"));
        assertThat(
            "Unexpected old value:", changeDescription10.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription10.getNewValue(), Matchers.is(2L));

        ChangeDescription changeDescription11 =
            getChange("crisprAttempt.guides.[2].sequence", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription11.getProperty(), is("crisprAttempt.guides.[2].sequence"));
        assertThat(
            "Unexpected old value:", changeDescription11.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription11.getNewValue(), Matchers.is("AAATCAATCTGCACAGTATTGGG"));

        ChangeDescription changeDescription12 =
            getChange("crisprAttempt.guides.[2].start", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription12.getProperty(), is("crisprAttempt.guides.[2].start"));
        assertThat(
            "Unexpected old value:", changeDescription12.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription12.getNewValue(), Matchers.is(9999999));

        ChangeDescription changeDescription13 =
            getChange("crisprAttempt.guides.[2].stop", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription13.getProperty(), is("crisprAttempt.guides.[2].stop"));
        assertThat(
            "Unexpected old value:", changeDescription13.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription13.getNewValue(), Matchers.is(999999999));

        ChangeDescription changeDescription14 =
            getChange("crisprAttempt.guides.[2].truncatedGuide", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription14.getProperty(), is("crisprAttempt.guides.[2].truncatedGuide"));
        assertThat(
            "Unexpected old value:", changeDescription14.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription14.getNewValue(), Matchers.is(false));
    }

    @Test
    public void testWhenGuidesDifferent()
    {
        Plan originalPlan = buildBasicPlan();
        Plan newPlan = buildBasicPlan();
        historyChangesAdaptor = new HistoryChangesAdaptor<>(originalPlan, newPlan);

        CrisprAttempt crisprAttempt1 = new CrisprAttempt();
        originalPlan.setCrisprAttempt(crisprAttempt1);

        CrisprAttempt crisprAttempt2 = new CrisprAttempt();
        newPlan.setCrisprAttempt(crisprAttempt2);

        Guide guide1 = new Guide();
        guide1.setId(1L);
        guide1.setChr("Y");
        guide1.setSequence("AGCCTCAATCTGCACAGTATTGGG");
        guide1.setStart(905880383);
        guide1.setStop(910880405);
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

        crisprAttempt1.setGuides(Collections.singleton(guide1));
        crisprAttempt2.setGuides(Collections.singleton(guide2));

        List<ChangeDescription> changeDescriptionList = historyChangesAdaptor.getChanges();

        assertThat("Unexpected number of changes:", changeDescriptionList.size(), is(14));

        ChangeDescription changeDescription1 =
            getChange("crisprAttempt.guides.[1]", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription1.getProperty(), is("crisprAttempt.guides.[1]"));
        assertThat(
            "Unexpected old value:", changeDescription1.getOldValue(), is(guide1));
        assertThat(
            "Unexpected new value:", changeDescription1.getNewValue(), Matchers.is(nullValue()));

        ChangeDescription changeDescription2 =
            getChange("crisprAttempt.guides.[1].chr", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription2.getProperty(), is("crisprAttempt.guides.[1].chr"));
        assertThat(
            "Unexpected old value:", changeDescription2.getOldValue(), is("Y"));
        assertThat(
            "Unexpected new value:", changeDescription2.getNewValue(), Matchers.is(nullValue()));

        ChangeDescription changeDescription3 =
            getChange("crisprAttempt.guides.[1].id", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription3.getProperty(), is("crisprAttempt.guides.[1].id"));
        assertThat(
            "Unexpected old value:", changeDescription3.getOldValue(), is(1L));
        assertThat(
            "Unexpected new value:", changeDescription3.getNewValue(), Matchers.is(nullValue()));

        ChangeDescription changeDescription4 =
            getChange("crisprAttempt.guides.[1].sequence", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription4.getProperty(), is("crisprAttempt.guides.[1].sequence"));
        assertThat(
            "Unexpected old value:", changeDescription4.getOldValue(), is("AGCCTCAATCTGCACAGTATTGGG"));
        assertThat(
            "Unexpected new value:", changeDescription4.getNewValue(), Matchers.is(nullValue()));

        ChangeDescription changeDescription5 =
            getChange("crisprAttempt.guides.[1].start", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription5.getProperty(), is("crisprAttempt.guides.[1].start"));
        assertThat(
            "Unexpected old value:", changeDescription5.getOldValue(), is(905880383));
        assertThat(
            "Unexpected new value:", changeDescription5.getNewValue(), Matchers.is(nullValue()));

        ChangeDescription changeDescription6 =
            getChange("crisprAttempt.guides.[1].stop", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription6.getProperty(), is("crisprAttempt.guides.[1].stop"));
        assertThat(
            "Unexpected old value:", changeDescription6.getOldValue(), is(910880405));
        assertThat(
            "Unexpected new value:", changeDescription6.getNewValue(), Matchers.is(nullValue()));

        ChangeDescription changeDescription7 =
            getChange("crisprAttempt.guides.[1].truncatedGuide", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription7.getProperty(), is("crisprAttempt.guides.[1].truncatedGuide"));
        assertThat(
            "Unexpected old value:", changeDescription7.getOldValue(), is(false));
        assertThat(
            "Unexpected new value:", changeDescription7.getNewValue(), Matchers.is(nullValue()));

        ChangeDescription changeDescription8 =
            getChange("crisprAttempt.guides.[2]", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription8.getProperty(), is("crisprAttempt.guides.[2]"));
        assertThat(
            "Unexpected old value:", changeDescription8.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription8.getNewValue(), Matchers.is(guide2));

        ChangeDescription changeDescription9 =
            getChange("crisprAttempt.guides.[2].chr", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription9.getProperty(), is("crisprAttempt.guides.[2].chr"));
        assertThat(
            "Unexpected old value:", changeDescription9.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription9.getNewValue(), Matchers.is("X"));

        ChangeDescription changeDescription10 =
            getChange("crisprAttempt.guides.[2].id", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription10.getProperty(), is("crisprAttempt.guides.[2].id"));
        assertThat(
            "Unexpected old value:", changeDescription10.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription10.getNewValue(), Matchers.is(2L));

        ChangeDescription changeDescription11 =
            getChange("crisprAttempt.guides.[2].sequence", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription11.getProperty(), is("crisprAttempt.guides.[2].sequence"));
        assertThat(
            "Unexpected old value:", changeDescription11.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription11.getNewValue(), Matchers.is("GCCTCAATCTGCACAGTATTGGG"));

        ChangeDescription changeDescription12 =
            getChange("crisprAttempt.guides.[2].start", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription12.getProperty(), is("crisprAttempt.guides.[2].start"));
        assertThat(
            "Unexpected old value:", changeDescription12.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription12.getNewValue(), Matchers.is(105880383));

        ChangeDescription changeDescription13 =
            getChange("crisprAttempt.guides.[2].stop", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription13.getProperty(), is("crisprAttempt.guides.[2].stop"));
        assertThat(
            "Unexpected old value:", changeDescription13.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription13.getNewValue(), Matchers.is(105880405));

        ChangeDescription changeDescription14 =
            getChange("crisprAttempt.guides.[2].truncatedGuide", changeDescriptionList);
        assertThat(
            "Unexpected property:", changeDescription14.getProperty(), is("crisprAttempt.guides.[2].truncatedGuide"));
        assertThat(
            "Unexpected old value:", changeDescription14.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription14.getNewValue(), Matchers.is(false));
    }

    @Test
    public void testWhenAddedPrimer()
    {
        Plan originalPlan = buildBasicPlan();
        Plan newPlan = buildBasicPlan();
        historyChangesAdaptor = new HistoryChangesAdaptor<>(originalPlan, newPlan);

        CrisprAttempt crisprAttempt1 = new CrisprAttempt();
        originalPlan.setCrisprAttempt(crisprAttempt1);

        CrisprAttempt crisprAttempt2 = new CrisprAttempt();
        newPlan.setCrisprAttempt(crisprAttempt2);

        GenotypePrimer primer1 = new GenotypePrimer();
        primer1.setId(1L);
        primer1.setSequence("ACACCCCTAGTCTTGTGTCTCA");
        primer1.setName("Dnah10 KO F");
        Set<GenotypePrimer> genotypePrimers = new HashSet<>();
        genotypePrimers.add(primer1);
        crisprAttempt2.setPrimers(genotypePrimers);

        List<ChangeDescription> changeDescriptionList = historyChangesAdaptor.getChanges();

        assertThat("Unexpected number of changes:", changeDescriptionList.size(), is(4));

        ChangeDescription changeDescription1 =
            getChange("crisprAttempt.primers.[1]", changeDescriptionList);
        assertThat("Unexpected property:",
            changeDescription1.getProperty(),
            is("crisprAttempt.primers.[1]"));
        assertThat(
            "Unexpected old value:", changeDescription1.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription1.getNewValue(), is(primer1));

        ChangeDescription changeDescription2 =
            getChange("crisprAttempt.primers.[1]", changeDescriptionList);
        assertThat("Unexpected property:",
            changeDescription2.getProperty(),
            is("crisprAttempt.primers.[1]"));
        assertThat(
            "Unexpected old value:", changeDescription2.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription2.getNewValue(), is(primer1));

        ChangeDescription changeDescription3 =
            getChange("crisprAttempt.primers.[1].name", changeDescriptionList);
        assertThat("Unexpected property:",
            changeDescription3.getProperty(),
            is("crisprAttempt.primers.[1].name"));
        assertThat(
            "Unexpected old value:", changeDescription3.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription3.getNewValue(), is("Dnah10 KO F"));

        ChangeDescription changeDescription4 =
            getChange("crisprAttempt.primers.[1].sequence", changeDescriptionList);
        assertThat("Unexpected property:",
            changeDescription4.getProperty(),
            is("crisprAttempt.primers.[1].sequence"));
        assertThat(
            "Unexpected old value:", changeDescription4.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected new value:", changeDescription4.getNewValue(), is("ACACCCCTAGTCTTGTGTCTCA"));
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