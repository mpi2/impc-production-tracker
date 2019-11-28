/******************************************************************************
 Copyright 2019 EMBL - European Bioinformatics Institute

 Licensed under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 either express or implied. See the License for the specific
 language governing permissions and limitations under the
 License.
 */
package org.gentar.biology.plan;

import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.audit.history.HistoryService;
import org.gentar.security.abac.ResourceAccessChecker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.gentar.biology.plan.outcome.PlanOutcomeRepository;
import org.gentar.audit.history.History;
import org.gentar.biology.plan.engine.PlanUpdater;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class PlanServiceImpl implements PlanService
{
    private PlanRepository planRepository;
    private ResourceAccessChecker<Plan> resourceAccessChecker;
    private PlanUpdater planUpdater;
    private HistoryService historyService;

    private static final String READ_PLAN_ACTION = "READ_PLAN";
    private static final String PLAN_NOT_EXISTS_ERROR =
        "The plan[%s] does not exist.";

    PlanServiceImpl(
        PlanRepository planRepository,
        ResourceAccessChecker<Plan> resourceAccessChecker,
        PlanUpdater planUpdater,
        HistoryService historyService,
        PlanOutcomeRepository planOutcomeRepository)
    {
        this.planRepository = planRepository;
        this.resourceAccessChecker = resourceAccessChecker;
        this.planUpdater = planUpdater;
        this.historyService = historyService;
    }

    @Override
    public List<Plan> getPlans(List<String> tpns, List<String> workUnitNames)
    {
        Specification<Plan> specifications =
            buildSpecificationsWithCriteria(tpns, workUnitNames);
        return getCheckedCollection(planRepository.findAll(specifications));
    }

    private Specification<Plan> buildSpecificationsWithCriteria(
        List<String> tpns, List<String> workUnitNames)
    {
        Specification<Plan> specifications =
            Specification.where(PlanSpecs.withTpns(tpns))
                .and(Specification.where(PlanSpecs.withWorkUnitNames(workUnitNames)));
        return specifications;
    }

    @Override
    public Page<Plan> getPlansBySpec(Specification<Plan> specification, Pageable pageable)
    {
        return planRepository.findAll(specification, pageable);
    }

    @Override
    public Plan getNotNullPlanByPin(String pin)
    throws UserOperationFailedException
    {
        Plan plan = planRepository.findPlanByPin(pin);
        if (plan == null)
        {
            throw new UserOperationFailedException(String.format(PLAN_NOT_EXISTS_ERROR, pin));
        }
        return plan;
    }

    @Override
    public Plan getPlanByPinWithoutCheckPermissions(String pin)
    {
        return planRepository.findPlanByPin(pin);
    }

    private List<Plan> getCheckedCollection(Collection<Plan> plans)
    {
        return plans.stream().map(this::getAccessChecked)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private Plan getAccessChecked(Plan plan)
    {
        return (Plan) resourceAccessChecker.checkAccess(plan, READ_PLAN_ACTION);
    }

    @Override
    public History updatePlan(String pin, Plan plan)
    {
        Plan existingPlan = getNotNullPlanByPin(pin);
        Plan originalPlan = new Plan(existingPlan);
        return planUpdater.updatePlan(originalPlan, plan);
    }

    @Override
    public List<History> getPlanHistory(Plan plan)
    {
        return historyService.getHistoryByEntityNameAndEntityId(Plan.class.getSimpleName(), plan.getId());
    }
}
