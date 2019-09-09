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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.common.history.HistoryService;
import uk.ac.ebi.impc_prod_tracker.conf.error_management.OperationFailedException;
import uk.ac.ebi.impc_prod_tracker.conf.security.abac.ResourceAccessChecker;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan_outcome.PlanOutcomeRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.PlanRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.common.history.History;
import uk.ac.ebi.impc_prod_tracker.service.plan.engine.PlanUpdater;
import uk.ac.ebi.impc_prod_tracker.service.plan.engine.UpdatePlanRequestProcessor;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.PlanDTO;
import java.util.ArrayList;
import java.util.List;

@Component
public class PlanServiceImpl implements PlanService
{
    private PlanRepository planRepository;
    private ResourceAccessChecker<Plan> resourceAccessChecker;
    private PlanUpdater planUpdater;
    private UpdatePlanRequestProcessor updatePlanRequestProcessor;
    private PlanOutcomeRepository planOutcomeRepository;
    private HistoryService historyService;

    private static final String READ_PLAN_ACTION = "READ_PLAN";
    private static final String PLAN_NOT_EXISTS_ERROR =
        "The plan[%s] does not exist.";

    PlanServiceImpl(
        PlanRepository planRepository,
        ResourceAccessChecker<Plan> resourceAccessChecker,
        PlanUpdater planUpdater,
        UpdatePlanRequestProcessor updatePlanRequestProcessor,
        HistoryService historyService,
        PlanOutcomeRepository planOutcomeRepository)
    {
        this.planRepository = planRepository;
        this.resourceAccessChecker = resourceAccessChecker;
        this.planUpdater = planUpdater;
        this.updatePlanRequestProcessor = updatePlanRequestProcessor;
        this.planOutcomeRepository = planOutcomeRepository;
        this.historyService = historyService;
    }

    @Override
    //TODO
    public Plan getProductionPlanRefByPhenotypePlan(Plan phenotypePlan)
    {
        Plan plan = null;
//        if (Constants.PHENOTYPE_TYPE.equals(phenotypePlan.getPlanType().getName()))
//        {
//            Attempt attempt = phenotypePlan.getAttempt();
//            PlanOutcome attemptParentOutcome = planOutcomeRepository.findByAttempt(attempt);
//            Long productionPlanId = attemptParentOutcome.getParentOutcome().getAttempt().getId();
//            plan = planRepository.findPlanById(productionPlanId);
//        }

        return plan;
    }

    @Override
    public Page<Plan> getPlansBySpec(Specification<Plan> specification, Pageable pageable)
    {
        return planRepository.findAll(specification, pageable);
    }

    @Override
    public Page<Plan> getPlansBySpecPro(Specification<Project> specification, Pageable pageable)
    {
        return null;
    }

    @Override
    public Plan getNotNullPlanByPin(String pin)
        throws OperationFailedException
    {
        Plan plan = planRepository.findPlanByPin(pin);
        if (plan == null)
        {
            throw new OperationFailedException(String.format(PLAN_NOT_EXISTS_ERROR, pin));
        }
        return plan;
    }


    @Override
    public List<Plan> getPlansByProject(Project project)
    {
        List<Plan> plans = new ArrayList<>();
        project.getPlans().forEach(plans::add);
        return getAccessCheckedPlans(plans);
    }

    @Override
    public Plan getPlanByPin(String pin)
    {
        Plan plan = planRepository.findPlanByPin(pin);
        return getAccessCheckedPlan(plan);
    }

    @Override
    public Plan getPlanByPinWithoutCheckPermissions(String pin)
    {
        return planRepository.findPlanByPin(pin);
    }

    @Override
    public List<Plan> getPlans()
    {
        List<Plan> plans = planRepository.findAll();
        return getAccessCheckedPlans(plans);
    }

    @Override
    public Page<Plan> getPaginatedPlans(Pageable pageable)
    {
        Page<Plan> plans = planRepository.findAll(pageable);
        return plans.map(this::getAccessCheckedPlan);
    }

    private Plan getAccessCheckedPlan(Plan plan)
    {
        // TODO revise restriction by plans
//        return (Plan) resourceAccessChecker.checkAccess(plan, READ_PLAN_ACTION);
        return  plan;
    }

    private List<Plan> getAccessCheckedPlans(List<Plan> plans)
    {
        // TODO revise restriction by plans
//        return (List<Plan>) resourceAccessChecker.checkAccessForCollection(plans, READ_PLAN_ACTION);
        return plans;
    }

    @Override
    public History updatePlan(String pin, PlanDTO planDTO)
    {
        Plan existingPlan = getNotNullPlanByPin(pin);
        Plan newPlan = new Plan(existingPlan);
        Plan originalPlan  = new Plan(existingPlan);

        newPlan = updatePlanRequestProcessor.getPlanToUpdate(newPlan, planDTO);

        return planUpdater.updatePlan(originalPlan, newPlan);
    }

    @Override
    public List<History> getPlanHistory(Plan plan)
    {
        return historyService.getHistoryByEntityNameAndEntityId(Plan.class.getSimpleName(), plan.getId());
    }
}
