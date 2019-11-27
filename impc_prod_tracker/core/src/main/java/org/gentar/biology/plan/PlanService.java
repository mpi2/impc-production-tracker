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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.gentar.audit.history.History;

import java.util.List;

public interface PlanService
{
    List<Plan> getPlans(List<String> tpns, List<String> workUnitNames);

    Plan getPlanByPinWithoutCheckPermissions(String pin);

    Page<Plan> getPlansBySpec(Specification<Plan> specification, Pageable pageable);

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

}
