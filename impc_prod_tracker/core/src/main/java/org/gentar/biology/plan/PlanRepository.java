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

import org.gentar.biology.project.Project;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PlanRepository extends PagingAndSortingRepository<Plan, Long>, JpaSpecificationExecutor<Plan>,
    CrudRepository<Plan, Long>
{

    List<Plan> findAll();

    List<Plan> findAllByProject(Project project);

    Plan findPlanByPin(String pin);

    Plan findPlanById(Long id);

    @Query("SELECT max(p.pin) FROM Plan p")
    String getMaxPin();
}