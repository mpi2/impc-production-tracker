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
import org.gentar.biology.plan.engine.PlanValidator;
import org.gentar.biology.plan.filter.PlanFilter;
import org.gentar.exceptions.NotFoundException;
import org.gentar.audit.history.HistoryService;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionAvailabilityEvaluator;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.gentar.audit.history.History;
import org.gentar.biology.plan.engine.PlanUpdater;
import java.util.List;

@Component
public class PlanServiceImpl implements PlanService
{
    private final PlanRepository planRepository;
    private final PlanUpdater planUpdater;
    private final HistoryService<Plan> historyService;
    private final PlanCreator planCreator;
    private final PlanStateMachineResolver planStateMachineResolver;
    private final TransitionAvailabilityEvaluator transitionAvailabilityEvaluator;
    private final PlanValidator planValidator;

    private static final String PLAN_NOT_EXISTS_ERROR =
        "The plan[%s] does not exist.";

    PlanServiceImpl(
        PlanRepository planRepository,
        PlanUpdater planUpdater,
        HistoryService<Plan> historyService,
        PlanCreator planCreator,
        PlanStateMachineResolver planStateMachineResolver,
        TransitionAvailabilityEvaluator transitionAvailabilityEvaluator,
        PlanValidator planValidator)
    {
        this.planRepository = planRepository;
        this.planUpdater = planUpdater;
        this.historyService = historyService;
        this.planCreator = planCreator;
        this.planStateMachineResolver = planStateMachineResolver;
        this.transitionAvailabilityEvaluator = transitionAvailabilityEvaluator;
        this.planValidator = planValidator;
    }

    @Override
    public Plan getNotNullPlanByPin(String pin)
    throws NotFoundException
    {
        Plan plan = planRepository.findPlanByPin(pin);
        if (plan == null)
        {
            throw new NotFoundException(String.format(PLAN_NOT_EXISTS_ERROR, pin));
        }
        planValidator.validateReadPermissions(plan);
        return planValidator.getAccessChecked(plan);
    }

    @Override
    public List<Plan> getPlans(PlanFilter planFilter)
    {
        Specification<Plan> specifications = buildSpecificationsWithCriteria(planFilter);
        List<Plan> plans = planRepository.findAll(specifications);
        return planValidator.getCheckedCollection(plans);
    }

    @Override
    public Page<Plan> getPageablePlans(Pageable page, PlanFilter planFilter)
    {
        Specification<Plan> specifications = buildSpecificationsWithCriteria(planFilter);
        Page<Plan> plans = planRepository.findAll(specifications, page);
        return plans;
        //List<Plan> plans = getPlans(planFilter);
        //return new PageImpl<>(plans, page, plans.size());
    }

    private Specification<Plan> buildSpecificationsWithCriteria(PlanFilter planFilter)
    {
        Specification<Plan> specifications =
            Specification.where(PlanSpecs.withProjectTpns(planFilter.getTpns()))
                .and(Specification.where(PlanSpecs.withWorkUnitNames(planFilter.getWorkUnitNames())))
                .and(Specification.where(PlanSpecs.withWorkGroupNames(planFilter.getWorGroupNames())))
                .and(Specification.where(PlanSpecs.withStatusNames(planFilter.getStatusNames())))
                .and(Specification.where(
                    PlanSpecs.withSummaryStatusNames(planFilter.getSummaryStatusNames())))
                .and(Specification.where(PlanSpecs.withPins(planFilter.getPins())))
                .and(Specification.where(PlanSpecs.withTypeNames(planFilter.getPlanTypeNames())))
                .and(Specification.where(
                    PlanSpecs.withAttemptTypeNames(planFilter.getAttemptTypeNames())))
                .and(Specification.where(
                    PlanSpecs.withImitsMiAttempts(planFilter.getImitsMiAttemptIds())))
                .and(Specification.where(
                    PlanSpecs.withImitsPhenotypeAttempts(planFilter.getImitsPhenotypeAttemptIds())))
                .and(Specification.where(
                        PlanSpecs.withPhenotypingExternalRefs(planFilter.getPhenotypingExternalRefs())))
                .and(Specification.where(
                        PlanSpecs.withDoNotCountTowardsCompleteness(planFilter.getDoNotCountTowardsCompleteness())));
        return specifications;
    }

    @Override
    public Plan getPlanByPinWithoutCheckPermissions(String pin)
    {
        return planRepository.findPlanByPin(pin);
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
        return historyService.getHistoryByEntityNameAndEntityId(
            Plan.class.getSimpleName(), plan.getId());
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

    @Override
    public ProcessEvent getProcessEventByName(Plan plan, String name)
    {
        return planStateMachineResolver.getProcessEventByActionName(plan, name);
    }

}
