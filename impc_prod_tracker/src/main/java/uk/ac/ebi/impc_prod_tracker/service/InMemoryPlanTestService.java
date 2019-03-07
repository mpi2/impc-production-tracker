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
package uk.ac.ebi.impc_prod_tracker.service;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.security.abac.spring.ContextAwarePolicyEnforcement;
import uk.ac.ebi.impc_prod_tracker.domain.PlanTest;
import uk.ac.ebi.impc_prod_tracker.domain.ProductionCentre;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InMemoryPlanTestService implements PlanTestService
{
    private ContextAwarePolicyEnforcement policy;

    public InMemoryPlanTestService(ContextAwarePolicyEnforcement policy)
    {
        this.policy = policy;
    }

    @Override
    public List<PlanTest> getPlans()
    {
        List<PlanTest> planTests = getAllPlans();
        return filterPlansWithPermissions(planTests);
    }

    private List<PlanTest> getAllPlans()
    {
        ProductionCentre pc1 = new ProductionCentre(1, "PC1");
        ProductionCentre pc2 = new ProductionCentre(2, "PC2");
        ProductionCentre pc3 = new ProductionCentre(3, "PC3");
        List<PlanTest> planTests = new ArrayList<>();
        planTests.add(new PlanTest(1, "gene1", pc1,"public"));
        planTests.add(new PlanTest(2, "gene2", pc2,"private"));
        planTests.add(new PlanTest(3, "gene3", pc3,"protected"));

        return planTests;
    }

    private List<PlanTest> getPublicPlans(List<PlanTest> planTests)
    {
        List<PlanTest> publicPlanTests = planTests.stream()
            .filter(p -> p.getPrivacy().equals("public")).collect(Collectors.toList());
        return publicPlanTests;
    }

    private List<PlanTest> filterPlansWithPermissions(List<PlanTest> planTests)
    {
        List<PlanTest> filteredPlanTests = planTests;
        boolean isUserAnonymous = policy.isUserAnonymous();
        if (isUserAnonymous)
        {
            filteredPlanTests = getPublicPlans(planTests);
        }
        else
        {
            for (PlanTest p : planTests)
            {
                if (!policy.hasPermission(p, "PLAN_LIST"))
                {
                    p.setGene("--------");
                }
            }
        }
        return filteredPlanTests;
    }
}
