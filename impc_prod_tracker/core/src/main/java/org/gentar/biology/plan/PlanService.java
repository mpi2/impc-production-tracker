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

import org.gentar.audit.history.History;
import org.gentar.biology.plan.filter.PlanFilter;
import org.gentar.statemachine.TransitionEvaluation;

import java.util.List;

public interface PlanService
{
    List<Plan> getPlans(PlanFilter planFilter);

    Plan getPlanByPinWithoutCheckPermissions(String pin);

    Plan getNotNullPlanByPin(String pin);

    /**
     * Updates a plan.
     * @param pin Identifier of the plan.
     * @param plan Values to update.
     */
    History updatePlan(String pin, Plan plan);

    /**
     * Gets the history for a plan
     * @param plan The plan.
     * @return List of {@link History} with the trace of the changes for a plan.
     */
    List<History> getPlanHistory(Plan plan);

    Plan createPlan(Plan plan);

    /**
     * Evaluates the transitions for a plan given its current status. To do that, this
     * method resolves the correct state machine for this plan and then checks what are the
     * possible transitions, evaluating each one and seeing if they could be executed
     * by the user or not.
     * @param plan Plan to evaluate.
     * @return The list of TransitionEvaluation that informs for each transition if it can
     * be executed or not, as long as a note explaining why in case it cannot be executed.
     */
    List<TransitionEvaluation> evaluateNextTransitions(Plan plan);
}
