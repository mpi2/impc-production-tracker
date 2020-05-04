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

import org.gentar.biology.plan.engine.PlanCreator;
import org.gentar.biology.plan.engine.PlanStateMachineResolver;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.audit.history.HistoryService;
import org.gentar.security.abac.ResourceAccessChecker;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionAvailabilityEvaluator;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
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
    private PlanCreator planCreator;
    private PlanStateMachineResolver planStateMachineResolver;
    private TransitionAvailabilityEvaluator transitionAvailabilityEvaluator;

    private static final String READ_PLAN_ACTION = "READ_PLAN";
    private static final String PLAN_NOT_EXISTS_ERROR =
        "The plan[%s] does not exist.";

    PlanServiceImpl(
        PlanRepository planRepository,
        ResourceAccessChecker<Plan> resourceAccessChecker,
        PlanUpdater planUpdater,
        HistoryService historyService,
        PlanCreator planCreator,
        PlanStateMachineResolver planStateMachineResolver,
        TransitionAvailabilityEvaluator transitionAvailabilityEvaluator)
    {
        this.planRepository = planRepository;
        this.resourceAccessChecker = resourceAccessChecker;
        this.planUpdater = planUpdater;
        this.historyService = historyService;
        this.planCreator = planCreator;
        this.planStateMachineResolver = planStateMachineResolver;
        this.transitionAvailabilityEvaluator = transitionAvailabilityEvaluator;
    }

    @Override
    public List<Plan> getPlans(List<String> projectTpns, List<String> workUnitNames, List<String> workGroupNames, List<String> statuses, List<String> pins, List<String> planTypeNames,
                               List<String> attemptTypeNames, List<String> imitsMiAttempts, List<String> imitsPhenotypeAttempts)
    {
        Specification<Plan> specifications =
            buildSpecificationsWithCriteria(projectTpns, workUnitNames, workGroupNames, statuses, pins, planTypeNames, attemptTypeNames, imitsMiAttempts, imitsPhenotypeAttempts);
        return getCheckedCollection(planRepository.findAll(specifications));
    }

    private Specification<Plan> buildSpecificationsWithCriteria(
        List<String> tpns, List<String> workUnitNames, List<String> workGroupNames, List<String> summaryStatusNames, List<String> pins, List<String> typeNames,
        List<String> attemptTypeNames, List<String> imitsMiAttempts, List<String> imitsPhenotypeAttempts)
    {
        Specification<Plan> specifications =
            Specification.where(PlanSpecs.withProjectTpns(tpns))
                .and(Specification.where(PlanSpecs.withWorkUnitNames(workUnitNames)))
                .and(Specification.where(PlanSpecs.withWorkGroupNames(workGroupNames)))
                .and(Specification.where(PlanSpecs.withSummaryStatusNames(summaryStatusNames)))
                .and(Specification.where(PlanSpecs.withPins(pins)))
                .and(Specification.where(PlanSpecs.withTypeNames(typeNames)))
                .and(Specification.where(PlanSpecs.withAttemptTypeNames(attemptTypeNames)))
                .and(Specification.where(PlanSpecs.withImitsMiAttempts(imitsMiAttempts)))
                .and(Specification.where(PlanSpecs.withImitsPhenotypeAttempts(imitsPhenotypeAttempts)));
        return specifications;
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

    @Override
    public Plan createPlan(Plan plan)
    {
        return planCreator.createPlan(plan);
    }

    @Override
    public List<TransitionEvaluation> evaluateNextTransitions(Plan plan)
    {
        List<ProcessEvent> transitions =
            planStateMachineResolver.getAvailableTransitionsByEntityStatus(plan);
        return transitionAvailabilityEvaluator.evaluate(transitions, plan);
    }
}
