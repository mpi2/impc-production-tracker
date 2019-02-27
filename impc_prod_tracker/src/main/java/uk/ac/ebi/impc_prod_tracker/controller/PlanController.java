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
package uk.ac.ebi.impc_prod_tracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.impc_prod_tracker.domain.Plan;
import uk.ac.ebi.impc_prod_tracker.service.PlanService;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PlanController
{
    private PlanService planService;

    public PlanController(PlanService planService)
    {
        this.planService = planService;
    }

    @GetMapping(value = {"/plans"})
    public List<Plan> getPlans()
    {
        return planService.getPlans();
    }
}
