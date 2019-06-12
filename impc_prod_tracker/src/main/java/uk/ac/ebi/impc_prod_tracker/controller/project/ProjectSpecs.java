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
package uk.ac.ebi.impc_prod_tracker.controller.project;

import org.springframework.data.jpa.domain.Specification;
import uk.ac.ebi.impc_prod_tracker.data.biology.intented_mouse_gene.IntendedMouseGene;
import uk.ac.ebi.impc_prod_tracker.data.biology.intented_mouse_gene.IntendedMouseGene_;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan_;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project_;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_group.WorkGroup;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_group.WorkGroup_;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit_;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.SetJoin;
import java.util.ArrayList;
import java.util.List;

/**
 * This class creates the filters needed when searching projects.
 */
public class ProjectSpecs
{
    /**
     * Get all the projects which plans are related with the work units specified in workUnitNames
     * @param workUnitNames List of names of the Work Units
     * @return The found projects. If workUnitNames is null then not filter is applied.
     */
    public static Specification<Project> getProjectsByWorkUnitNames(List<String> workUnitNames)
    {
        return (Specification<Project>) (root, query, criteriaBuilder) -> {
            if (workUnitNames == null)
            {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            List<Predicate> predicates = new ArrayList<>();

            SetJoin<Project, Plan> plansJoin = root.join(Project_.plans);
            Path<WorkUnit> workUnitPath = plansJoin.get(Plan_.workUnit);
            Path<String> workUnitName = workUnitPath.get(WorkUnit_.name);
            predicates.add(workUnitName.in(workUnitNames));

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    /**
     * Get all the projects which related genes have the marker symbols defined in parameter
     * markerSymbols.
     * @param markerSymbols List of names of the marker symbols
     * @return The found projects. If markerSymbols is null then not filter is applied.
     */
    public static Specification<Project> getProjectsByMarkerSymbol(List<String> markerSymbols)
    {
        return (Specification<Project>) (root, query, criteriaBuilder) -> {
            if (markerSymbols == null)
            {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            List<Predicate> predicates = new ArrayList<>();

            SetJoin<Project, IntendedMouseGene> intendedMouseGeneSetJoin =
                root.join(Project_.intendedMouseGenes);
            Path<String> symbolName = intendedMouseGeneSetJoin.get(IntendedMouseGene_.symbol);
            predicates.add(symbolName.in(markerSymbols));

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    /**
     * Get all the projects which plans are related with the work units specified in workGroupNames
     * @param workGroupsNames List of names of Work Group
     * @return The found projects. If markerSymbols is null then not filter is applied.
     */
    public static Specification<Project> getProjectsByWorkGroup(List<String> workGroupsNames)
    {
        return (Specification<Project>) (root, query, criteriaBuilder) -> {
            if (workGroupsNames == null)
            {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            List<Predicate> predicates = new ArrayList<>();

            SetJoin<Project, Plan> plansJoin = root.join(Project_.plans);
            Path<WorkGroup> workGroupPath = plansJoin.get(Plan_.workGroup);
            Path<String> workGroupName = workGroupPath.get(WorkGroup_.name);
            predicates.add(workGroupName.in(workGroupsNames));

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
