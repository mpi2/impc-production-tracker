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

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.Plan_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.Project_;
import org.gentar.organization.work_unit.WorkUnit;
import org.gentar.organization.work_unit.WorkUnit_;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
public class PlanSpecs
{
    /**
     * Get all the projects which plans are related with the work units specified in workUnitNames
     * @param workUnitNames List of names of the Work Units
     * @return The found projects. If workUnitNames is null then not filter is applied.
     */
    public static Specification<Plan> withWorkUnitNames(List<String> workUnitNames)
    {
        Specification<Plan> specification;
        if (workUnitNames == null)
        {
            specification = buildTrueCondition();
        }
        else
        {
            specification = (Specification<Plan>) (root, query, criteriaBuilder) ->
            {
                List<Predicate> predicates = new ArrayList<>();
                Path<WorkUnit> workUnitPath = root.get(Plan_.workUnit);
                Path<String> workUnitName = workUnitPath.get(WorkUnit_.name);
                predicates.add(workUnitName.in(workUnitNames));
                query.distinct(true);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            };

        }
        return specification;
    }

    public static Specification<Plan> withTpns(List<String> tpns)
    {
        Specification<Plan> specification;
        if (tpns == null)
        {
            specification = buildTrueCondition();
        }
        else
        {
            specification = (Specification<Plan>) (root, query, criteriaBuilder) ->
            {
                List<Predicate> predicates = new ArrayList<>();
                Path<Project> projectPath = root.get(Plan_.project);
                Path<String> tpnPath = projectPath.get(Project_.tpn);
                predicates.add(tpnPath.in(tpns));
                query.distinct(true);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            };

        }
        return specification;
    }

    private static Specification<Plan> buildTrueCondition()
    {
        return (Specification<Plan>) (root, query, criteriaBuilder) ->
            criteriaBuilder.isTrue(criteriaBuilder.literal(true));
    }
}
