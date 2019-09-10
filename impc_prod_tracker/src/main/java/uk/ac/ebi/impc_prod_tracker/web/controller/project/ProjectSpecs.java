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
package uk.ac.ebi.impc_prod_tracker.web.controller.project;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.security.abac.spring.SubjectRetriever;
import uk.ac.ebi.impc_prod_tracker.data.biology.assignment_status.AssignmentStatus;
import uk.ac.ebi.impc_prod_tracker.data.biology.assignment_status.AssignmentStatus_;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan_;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.type.PlanType;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.type.PlanType_;
import uk.ac.ebi.impc_prod_tracker.data.biology.privacy.Privacy;
import uk.ac.ebi.impc_prod_tracker.data.biology.privacy.Privacy_;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project_;
import uk.ac.ebi.impc_prod_tracker.data.biology.status.Status;
import uk.ac.ebi.impc_prod_tracker.data.biology.status.Status_;
import uk.ac.ebi.impc_prod_tracker.data.organization.consortium.Consortium;
import uk.ac.ebi.impc_prod_tracker.data.organization.consortium.Consortium_;
import uk.ac.ebi.impc_prod_tracker.data.organization.person_role_work_unit.PersonRoleWorkUnit;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit_;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.SetJoin;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class creates the filters needed when searching projects.
 */
@Component
public class ProjectSpecs
{
    private SubjectRetriever subjectRetriever;

    public ProjectSpecs(SubjectRetriever subjectRetriever)
    {
        this.subjectRetriever = subjectRetriever;
    }

    public Specification<Project> withPlansInUserWorkUnits()
    {
        Specification<Project> specification;

        if (subjectRetriever.getSubject().isAdmin())
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder)
                -> criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        }
        else
        {
            List<WorkUnit> workUnits =
                subjectRetriever.getSubject().getRoleWorkUnits().stream()
                    .map(PersonRoleWorkUnit::getWorkUnit)
                    .collect(Collectors.toList());
            List<String> workUnitNames = new ArrayList<>();
            workUnits.forEach(x -> workUnitNames.add(x.getName()));
            if (workUnits.isEmpty())
            {
                specification = (Specification<Project>) (root, query, criteriaBuilder)
                    -> criteriaBuilder.isTrue(criteriaBuilder.literal(false));
            }
            else
            {
                specification = getProjectsByWorkUnitNames(workUnitNames);
            }
        }
        return specification;
    }

    /**
     * Get all the projects which plans are related with the work units specified in workUnitNames
     * @param workUnitNames List of names of the Work Units
     * @return The found projects. If workUnitNames is null then not filter is applied.
     */
    private static Specification<Project> getProjectsByWorkUnitNames(List<String> workUnitNames)
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
            query.distinct(true);

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    /**
     * Get all the projects which related genes have the marker symbols defined in parameter
     * markerSymbols.
     * @param markerSymbols List of names of the marker symbols
     * @return The found projects. If markerSymbols is null then not filter is applied.
     */
    public static Specification<Project> getProjectsByMarkerSymbolAndSpecie(List<String> markerSymbols)
    {
        return (Specification<Project>) (root, query, criteriaBuilder) -> {
            if (markerSymbols == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            List<Predicate> predicates = new ArrayList<>();


//            SetJoin<Project, ProjectMouseGene> projectSetJoin = root.join(Project_.projectMouseGenes);
//            Path<IntendedMouseGene> intendedMouseGenePath = projectSetJoin.get(ProjectMouseGene_.gene);
//            Path<String> symbolName = intendedMouseGenePath.get(IntendedMouseGene_.symbol);
//            predicates.add(symbolName.in(markerSymbols));

            query.distinct(true);


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
            //TODO: Filter at project level
//            Path<WorkGroup> workGroupPath = plansJoin.get(Plan_.workGroup);
//            Path<String> workGroupName = workGroupPath.get(WorkGroup_.name);
//            predicates.add(workGroupName.in(workGroupsNames));
            query.distinct(true);

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    /**
     * Get all the projects which plans of the specified type in planTypes.
     * @param planTypes List of plan types.
     * @return The found projects. If planTypes is null then not filter is applied.
     */
    public static Specification<Project> getProjectsByPlanType(List<String> planTypes)
    {
        return (Specification<Project>) (root, query, criteriaBuilder) -> {
            if (planTypes == null)
            {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            List<Predicate> predicates = new ArrayList<>();

            SetJoin<Project, Plan> plansJoin = root.join(Project_.plans);
            Path<PlanType> planType = plansJoin.get(Plan_.planType);
            Path<String> planTypeName = planType.get(PlanType_.name);
            predicates.add(planTypeName.in(planTypes));
            query.distinct(true);

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    /**
     * Get all the projects whose status is one of the list of statuses provided.
     * @param statuses List of names of statuses.
     * @return The found projects. If statuses is null then not filter is applied.
     */
    public Specification<Project> withStatuses(List<String> statuses)
    {
        Specification<Project> specification;
        if (statuses == null)
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        }
        else
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder) -> {

                List<Predicate> predicates = new ArrayList<>();

                Path<AssignmentStatus> status = root.get(Project_.assignmentStatus);
                Path<String> statusName = status.get(AssignmentStatus_.name);
                predicates.add(statusName.in(statuses));
                query.distinct(true);

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            };
        }
        return specification;
    }

    /**
     * Get all the projects with a specific privacy (or privacies).
     * @param privacies List of names of privacies.
     * @return The found projects. If privacies is null then not filter is applied.
     */
    public Specification<Project> withPrivacies(List<String> privacies)
    {
        Specification<Project> specification;
        if (privacies == null)
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        }
        else
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder) -> {

                List<Predicate> predicates = new ArrayList<>();

                Path<Privacy> privacy = root.get(Project_.privacy);
                Path<String> privacyName = privacy.get(Privacy_.name);
                predicates.add(privacyName.in(privacies));
                query.distinct(true);

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            };
        }
        return specification;
    }

    /**
     * Get all the projects with a specific consortium (or consortia).
     * @param consortia List of names of consortia.
     * @return The found projects. If privacies is null then not filter is applied.
     */
    public Specification<Project> withConsortia(List<String> consortia)
    {
        Specification<Project> specification;
        if (consortia == null)
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        }
        else
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder) -> {

                List<Predicate> predicates = new ArrayList<>();

                SetJoin<Project, Consortium> consortiumSetJoin = root.join(Project_.consortia);
                Path<String> consortiumName = consortiumSetJoin.get(Consortium_.name);
                predicates.add(consortiumName.in(consortia));
                query.distinct(true);

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            };
        }
        return specification;
    }
}
