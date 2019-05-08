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
package uk.ac.ebi.impc_prod_tracker.service.plan;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.PlanRepository;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project;
import java.util.ArrayList;
import java.util.List;

@Component
public class PlanServiceImpl implements PlanService
{
    private PlanRepository planRepository;

    PlanServiceImpl(PlanRepository planRepository)
    {
        this.planRepository = planRepository;
    }

    @Override
    public List<Plan> getPlansByProject(Project project)
    {
        Iterable<Plan> plans = planRepository.findAllByProject(project);
        List<Plan> planList = new ArrayList<>();
        plans.forEach(planList::add);
        return planList;
    }

    @Override
    public Plan getPlanByPin(String pin)
    {
        return planRepository.findPlanByPin(pin);
    }

    @Override
    public List<Plan> getPlans()
    {
        List<Plan> planList = new ArrayList<>();
        Iterable<Plan> plans = planRepository.findAll();
        plans.forEach(planList::add);
        return planList;
    }
}
