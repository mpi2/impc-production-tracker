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
package uk.ac.ebi.impc_prod_tracker.service.biology.project;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_type.AlleleType;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_type.AlleleType_;
import uk.ac.ebi.impc_prod_tracker.data.biology.assignment_status.AssignmentStatus;
import uk.ac.ebi.impc_prod_tracker.data.biology.assignment_status.AssignmentStatus_;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.Gene;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.Gene_;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan_;
import uk.ac.ebi.impc_prod_tracker.data.biology.privacy.Privacy;
import uk.ac.ebi.impc_prod_tracker.data.biology.privacy.Privacy_;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project_;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_gene.ProjectIntentionGene;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention.ProjectIntention;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention.ProjectIntention_;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_gene.ProjectIntentionGene_;
import uk.ac.ebi.impc_prod_tracker.data.organization.consortium.Consortium;
import uk.ac.ebi.impc_prod_tracker.data.organization.consortium.Consortium_;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit_;

import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.SetJoin;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class creates the filters needed when searching projects.
 */
@Component
public class ProjectSpecs
{
    /**
     * Get all the projects which related genes have the marker symbols defined in parameter
     * markerSymbols.
     *
     * @param markerSymbols List of names of the marker symbols
     * @return The found projects. If markerSymbols is null then not filter is applied.
     */
    public static Specification<Project> withMarkerSymbols(List<String> markerSymbols)
    {
        return (Specification<Project>) (root, query, criteriaBuilder) -> {
            if (markerSymbols == null)
            {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            List<Predicate> predicates = new ArrayList<>();
            ListJoin<Project, ProjectIntention> projectProjectIntentionSetJoin =
                root.join(Project_.projectIntentions);

            Path<ProjectIntentionGene> projectIntentionProjectGeneSetJoin =
                projectProjectIntentionSetJoin.get(ProjectIntention_.projectIntentionGene);
            Path<Gene> genePath = projectIntentionProjectGeneSetJoin.get(ProjectIntentionGene_.gene);
            Path<String> symbolName = genePath.get(Gene_.symbol);
            predicates.add(symbolName.in(markerSymbols));

            query.distinct(true);
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public static Specification<Project> withGenes(List<String> genesNameOrIds)
    {
        return (Specification<Project>) (root, query, criteriaBuilder) -> {
            if (genesNameOrIds == null)
            {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            List<String> lowerCaseGenesNameOrIds =
                genesNameOrIds.stream().map(String::toLowerCase).collect(Collectors.toList());

            ListJoin<Project, ProjectIntention> projectProjectIntentionSetJoin =
                root.join(Project_.projectIntentions);
            Path<ProjectIntentionGene> projectGenePath =
                projectProjectIntentionSetJoin.join(ProjectIntention_.projectIntentionGene);
            Path<Gene> genePath = projectGenePath.get(ProjectIntentionGene_.gene);
            Path<String> symbolNamePath = genePath.get(Gene_.symbol);
            Path<String> accIdPath = genePath.get(Gene_.accId);

            query.distinct(true);
            return criteriaBuilder.or(
                criteriaBuilder.lower(symbolNamePath).in(lowerCaseGenesNameOrIds),
                criteriaBuilder.lower(accIdPath).in(lowerCaseGenesNameOrIds)
            );
        };
    }

    public static Specification<Project> withIntentions(List<String> intentionNames)
    {
        Specification<Project> specification;

        if (intentionNames == null)
        {
            specification = buildTrueCondition();
        } else
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder) -> {

                ListJoin<Project, ProjectIntention> projectProjectIntentionSetJoin =
                    root.join(Project_.projectIntentions);

                Path<AlleleType> alleleTypePath =
                    projectProjectIntentionSetJoin.join(ProjectIntention_.alleleType);

                Path<String> alleleTypeName = alleleTypePath.get(AlleleType_.name);

                query.distinct(true);

                return alleleTypeName.in(intentionNames);
            };
        }
        return specification;
    }

    /**
     * Get all the projects which plans are related with the work units specified in workUnitNames
     *
     * @param workUnitNames List of names of the Work Units
     * @return The found projects. If workUnitNames is null then not filter is applied.
     */
    public static Specification<Project> withPlansInWorkUnitsNames(List<String> workUnitNames)
    {
        Specification<Project> specification;

        if (workUnitNames == null)
        {
            specification = buildTrueCondition();
        } else
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder) -> {

                List<Predicate> predicates = new ArrayList<>();

                SetJoin<Project, Plan> plansJoin = root.join(Project_.plans);
                Path<WorkUnit> workUnitPath = plansJoin.get(Plan_.workUnit);
                Path<String> workUnitName = workUnitPath.get(WorkUnit_.name);
                predicates.add(workUnitName.in(workUnitNames));
                query.distinct(true);

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            };
        }
        return specification;

    }

    /**
     * Get all the projects whose status is one of the list of statuses provided.
     *
     * @param statuses List of names of statuses.
     * @return The found projects. If statuses is null then not filter is applied.
     */
    public static Specification<Project> withStatuses(List<String> statuses)
    {
        Specification<Project> specification;
        if (statuses == null)
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        } else
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
     *
     * @param privacies List of names of privacies.
     * @return The found projects. If privacies is null then not filter is applied.
     */
    public static Specification<Project> withPrivacies(List<String> privacies)
    {
        Specification<Project> specification;
        if (privacies == null)
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        } else
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
     *
     * @param consortia List of names of consortia.
     * @return The found projects. If privacies is null then not filter is applied.
     */
    public static Specification<Project> withConsortia(List<String> consortia)
    {
        Specification<Project> specification;
        if (consortia == null)
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        } else
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

    /**
     * Get all the projects with a specific tpn (or tpns).
     *
     * @param tpns List of names of tpn.
     * @return The found projects. If tpn is null then not filter is applied.
     */
    public static Specification<Project> withTpns(List<String> tpns)
    {
        Specification<Project> specification;
        if (tpns == null)
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        } else
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder) -> {

                List<Predicate> predicates = new ArrayList<>();
                Path<String> tpn = root.get(Project_.tpn);
                predicates.add(tpn.in(tpns));
                query.distinct(true);

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            };
        }
        return specification;
    }

    private static Specification<Project> buildTrueCondition()
    {
        return (Specification<Project>) (root, query, criteriaBuilder) ->
            criteriaBuilder.isTrue(criteriaBuilder.literal(true));
    }
}
