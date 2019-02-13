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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.security.abac.spring.ContextAwarePolicyEnforcement;
import uk.ac.ebi.impc_prod_tracker.domain.Plan;
import uk.ac.ebi.impc_prod_tracker.domain.ProductionCentre;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InMemoryPlanService implements PlanService
{
    @Autowired
    private ContextAwarePolicyEnforcement policy;

    @Override
    public List<Plan> getPlans()
    {
        List<Plan> plans = getAllPlans();
        return filterPlansWithPermissions(plans);
    }

    private List<Plan> getAllPlans()
    {
        ProductionCentre pc1 = new ProductionCentre(1, "PC1");
        ProductionCentre pc2 = new ProductionCentre(2, "PC2");
        ProductionCentre pc3 = new ProductionCentre(3, "PC3");
        List<Plan> plans = new ArrayList<>();
        plans.add(new Plan(1, "gene1", pc1,"public"));
        plans.add(new Plan(2, "gene2", pc2,"private"));
        plans.add(new Plan(3, "gene3", pc3,"protected"));

        return plans;
    }

    private List<Plan> getPublicPlans(List<Plan> plans)
    {
        List<Plan> publicPlans = plans.stream()
            .filter(p -> p.getPrivacy().equals("public")).collect(Collectors.toList());
        return publicPlans;
    }

    private List<Plan> filterPlansWithPermissions(List<Plan> plans)
    {
        List<Plan> filteredPlans = plans;
        boolean isUserAnonymous = policy.isUserAnonymous();
        if (isUserAnonymous)
        {
            filteredPlans = getPublicPlans(plans);
        }
        else
        {
            for (Plan p : plans)
            {
                if (!policy.hasPermission(p, "PLAN_LIST"))
                {
                    p.setGene("--------");
                }
            }
        }
        return filteredPlans;
    }
}
