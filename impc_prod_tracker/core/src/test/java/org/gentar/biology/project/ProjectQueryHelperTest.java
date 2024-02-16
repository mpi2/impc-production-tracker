package org.gentar.biology.project;

import org.gentar.biology.intention.project_intention_gene.ProjectIntentionGene;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.type.PlanType;
import org.gentar.biology.plan.type.PlanTypeName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.gentar.mockdata.MockData.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ProjectQueryHelperTest {
    private ProjectQueryHelper testInstance;

    @BeforeEach
    void setup() {
        testInstance = new ProjectQueryHelper();
    }

    @Test
    void testGetProductionPlansByTypeWhenNoPlans() {
        Project project = new Project();
        List<Plan> plans = testInstance.getPlansByType(project, PlanTypeName.PRODUCTION);

        assertThat("Plans should be an empty list", plans.isEmpty(), is(true));
    }

    @Test
    void testGetPhenotypingPlansByTypeWhenNoPlans() {
        Project project = new Project();
        List<Plan> plans = testInstance.getPlansByType(project, PlanTypeName.PHENOTYPING);

        assertThat("Plans should be an empty list", plans.isEmpty(), is(true));
    }

    @Test
    void testGetProductionPlansByTypeWhenNoProductionPlans() {
        Project project = new Project();

        Plan phenotypingPlan = buildPlanByTypeName("pin1", PlanTypeName.PHENOTYPING);
        project.addPlan(phenotypingPlan);
        List<Plan> plans = testInstance.getPlansByType(project, PlanTypeName.PRODUCTION);

        assertThat("Plans should be an empty list", plans.isEmpty(), is(true));
    }

    @Test
    void testGetPhenotypingPlansByTypeWhenNoProductionPlans() {
        Project project = new Project();

        Plan productionPlan = buildPlanByTypeName("pin1", PlanTypeName.PRODUCTION);
        project.addPlan(productionPlan);
        List<Plan> plans = testInstance.getPlansByType(project, PlanTypeName.PHENOTYPING);

        assertThat("Plans should be an empty list", plans.isEmpty(), is(true));
    }

    @Test
    void testGetProductionPlans() {
        Project project = new Project();

        Plan plan1 = buildPlanByTypeName("pin1", PlanTypeName.PHENOTYPING);
        Plan plan2 = buildPlanByTypeName("pin2", PlanTypeName.PRODUCTION);
        Plan plan3 = buildPlanByTypeName("pin3", PlanTypeName.PRODUCTION);
        project.addPlan(plan1);
        project.addPlan(plan2);
        project.addPlan(plan3);
        List<Plan> plans = testInstance.getPlansByType(project, PlanTypeName.PRODUCTION);

        assertThat("Plans should not be an empty list", plans.isEmpty(), is(false));
        assertThat("Expected 2 plans", plans.size(), is(2));
        assertThat("Unexpected plan", plans.get(0).getPin(), is("pin2"));
        assertThat("Unexpected plan", plans.get(1).getPin(), is("pin3"));
    }

    @Test
    void testGetPhenotypingPlans() {
        Project project = new Project();

        Plan plan1 = buildPlanByTypeName("pin1", PlanTypeName.PRODUCTION);
        Plan plan2 = buildPlanByTypeName("pin2", PlanTypeName.PHENOTYPING);
        Plan plan3 = buildPlanByTypeName("pin3", PlanTypeName.PHENOTYPING);
        project.addPlan(plan1);
        project.addPlan(plan2);
        project.addPlan(plan3);
        List<Plan> plans = testInstance.getPlansByType(project, PlanTypeName.PHENOTYPING);

        assertThat("Plans should not be an empty list", plans.isEmpty(), is(false));
        assertThat("Expected 2 plans", plans.size(), is(2));
        assertThat("Unexpected plan", plans.get(0).getPin(), is("pin2"));
        assertThat("Unexpected plan", plans.get(1).getPin(), is("pin3"));
    }

    @Test
    void testGetIntentionGenesByProject() {

        List<ProjectIntentionGene> intentionGenes =
            testInstance.getIntentionGenesByProject(projectMockData());
        assertEquals(intentionGenes, projectIntentionGeneListMockData());
    }

    @Test
    void testGetAccIdsByProject() {

        List<String> accIds = testInstance.getAccIdsByProject(projectMockData());

        assertEquals(accIds, List.of(MGI_00000001));

    }

    private Plan buildPlanByTypeName(String pin, PlanTypeName planTypeName) {
        Plan plan = new Plan();
        plan.setPin(pin);
        PlanType planType = new PlanType();
        planType.setName(planTypeName.getLabel());
        plan.setPlanType(planType);
        return plan;
    }
}