package org.gentar.biology.project.search.filter;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.project.Project;
import org.gentar.organization.work_unit.WorkUnit;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class FluentProjectFilterTest
{
    private FluentProjectFilter testInstance;

    @Test
    public void noFilter()
    {
        List<Project> data = buildData();

        testInstance = new FluentProjectFilter(data);
        assertThat(
            "Data should be the same",
            testInstance.getFilteredData(),
            is(data));
    }

    @Test
    public void filterOneTpn()
    {
        List<Project> data = buildData();
        var w = new FluentProjectFilter(data).withTpns(Collections.singletonList("tpn1"));
        List<Project> filteredData = w.getFilteredData();
        System.out.println(filteredData);
        assertThat(
            "There should be only 1 result",
            filteredData.size(),
            is(1));
        assertThat(
            "Filtered value is incorrect",
            filteredData.get(0).getTpn(),
            is("tpn1"));
    }

    @Test
    public void filterOneWorkUnitName()
    {
        List<Project> data = buildData();
        WorkUnit workUnit = new WorkUnit("wu1");
        Plan plan = new Plan();
        plan.setWorkUnit(workUnit);
        data.get(0).setPlans(Collections.singleton(plan));
        var test =  new FluentProjectFilter(data).withWorkUnitNames(Collections.singletonList("wu1"));
        List<Project> filteredData = test.getFilteredData();
        System.out.println(filteredData);
        assertThat(
            "There should be only 1 result",
            filteredData.size(),
            is(1));

        assertThat(
            "Filtered value is incorrect",
            filteredData.get(0).getTpn(),
            is("tpn0"));
    }

    private List<Project> buildData()
    {
        List<Project> data = new ArrayList<>();
        for (int i = 0; i < 100; i++)
        {
            Project p = new Project();
            p.setTpn("tpn" + i);
            data.add(p);
        }
        return data;
    }
}