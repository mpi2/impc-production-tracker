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
package org.gentar.biology.project.specs;

import org.gentar.biology.project.Project;
import org.gentar.util.PredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import jakarta.persistence.criteria.Path;
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
        if (markerSymbols == null)
        {
            return null;
        }
        return (root, query, criteriaBuilder) -> {
            Path<String> symbolNamePath = ProjectPaths.getMarkerSymbolPath(root);
            query.distinct(true);
            return PredicateBuilder.addInPredicates(
                criteriaBuilder, symbolNamePath, markerSymbols);
        };
    }

    public static Specification<Project> withMarkerSymbolOrAccId(List<String> genesNameOrIds)
    {
        if (genesNameOrIds == null)
        {
            return null;
        }
        return (root, query, criteriaBuilder) -> {
            List<String> lowerCaseMarkerSymbolsOrIds =
                genesNameOrIds.stream().map(String::toLowerCase).collect(Collectors.toList());
            Path<String> symbolNamePath = ProjectPaths.getMarkerSymbolPath(root);
            Path<String> accIdPath = ProjectPaths.getAccIdPath(root);

            query.distinct(true);
            return criteriaBuilder.or(
                criteriaBuilder.lower(symbolNamePath).in(lowerCaseMarkerSymbolsOrIds),
                criteriaBuilder.lower(accIdPath).in(lowerCaseMarkerSymbolsOrIds)
            );
        };
    }

    public static Specification<Project> withIntentions(List<String> intentionNames)
    {
        if (intentionNames == null)
        {
            return null;
        }
        return (root, query, criteriaBuilder) -> {
            Path<String> geneticMutationTypeNamePath = ProjectPaths.getMolecularMutationTypeNamePath(root);

            query.distinct(true);
            return PredicateBuilder.addInPredicates(
                criteriaBuilder, geneticMutationTypeNamePath, intentionNames);
        };
    }

    /**
     * Get all the projects which plans are related with the work units specified in workUnitNames
     *
     * @param workUnitNames List of names of the Work Units
     * @return The found projects. If workUnitNames is null then not filter is applied.
     */
    public static Specification<Project> withPlansInWorkUnitsNames(List<String> workUnitNames)
    {
        if (workUnitNames == null)
        {
            return null;
        }
        return (root, query, criteriaBuilder) -> {
            Path<String> workUnitNamePath = ProjectPaths.getWorkUnitNamePath(root);
            query.distinct(true);
            return PredicateBuilder.addInPredicates(
                criteriaBuilder, workUnitNamePath, workUnitNames);
        };
    }

    /**
     * Get all the projects which plans are related with the work groups specified in workUnitNames
     *
     * @param workGroupNames List of names of the Work Groups
     * @return The found projects. If workUnitNames is null then not filter is applied.
     */
    public static Specification<Project> withPlansInWorkGroupNames(List<String> workGroupNames)
    {
        if (workGroupNames == null)
        {
            return null;
        }
        return (root, query, criteriaBuilder) -> {
            Path<String> workGroupNamePath = ProjectPaths.getWorkGroupNamePath(root);
            query.distinct(true);
            return PredicateBuilder.addInPredicates(
                criteriaBuilder, workGroupNamePath, workGroupNames);
        };
    }

    /**
     * Get all the projects whose status is one of the list of statuses provided.
     *
     * @param statuses List of names of statuses.
     * @return The found projects. If statuses is null then not filter is applied.
     */
    public static Specification<Project> withAssignments(List<String> statuses)
    {
        if (statuses == null)
        {
            return null;
        }
        return (root, query, criteriaBuilder) -> {
            Path<String> statusNamePath = ProjectPaths.getAssignmentNamePath(root);
            query.distinct(true);
            return PredicateBuilder.addInPredicates(criteriaBuilder, statusNamePath, statuses);
        };
    }

    /**
     * Get all the projects whose summary status is one of the list of statuses provided.
     *
     * @param summaryStatuses List of names of statuses.
     * @return The found projects. If statuses is null then not filter is applied.
     */
    public static Specification<Project> withSummaryStatuses(List<String> summaryStatuses)
    {
        if (summaryStatuses == null)
        {
            return null;
        }
        return (root, query, criteriaBuilder) -> {
            Path<String> statusNamePath = ProjectPaths.getSummaryStatusNamePath(root);
            query.distinct(true);
            return PredicateBuilder.addInPredicates(criteriaBuilder, statusNamePath, summaryStatuses);
        };
    }

    /**
     * Get all the projects with a specific privacy (or privacies).
     *
     * @param privacies List of names of privacies.
     * @return The found projects. If privacies is null then not filter is applied.
     */
    public static Specification<Project> withPrivacies(List<String> privacies)
    {
        if (privacies == null)
        {
            return null;
        }
        return (root, query, criteriaBuilder) -> {
            Path<String> privacyNamePath = ProjectPaths.getPrivacyNamePath(root);
            query.distinct(true);
            return PredicateBuilder.addInPredicates(criteriaBuilder, privacyNamePath, privacies);
        };
    }

    /**
     * Get all the projects with a specific consortium (or consortia).
     *
     * @param consortia List of names of consortia.
     * @return The found projects. If privacies is null then not filter is applied.
     */
    public static Specification<Project> withConsortia(List<String> consortia)
    {
        if (consortia == null)
        {
            return null;
        }
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Path<String> consortiumNamePath = ProjectPaths.getConsortiaNamePath(root);
            return PredicateBuilder.addInPredicates(
                criteriaBuilder, consortiumNamePath, consortia);
        };
    }

    /**
     * Get all the projects with a specific tpn (or tpns).
     *
     * @param tpns List of names of tpn.
     * @return The found projects. If tpn is null then not filter is applied.
     */
    public static Specification<Project> withTpns(List<String> tpns)
    {
        if (tpns == null)
        {
            return null;
        }
        return (root, query, criteriaBuilder) -> {
            Path<String> tpnPath = ProjectPaths.getTpnPath(root);
            query.distinct(true);
            return PredicateBuilder.addLowerLikeOrPredicates(criteriaBuilder, tpnPath, tpns);
        };
    }

    /**
     * Get all the projects with a specific imitsMiPlanId (or imitsMiPlanIds).
     *
     * @param imitsMiPlans List of names of imits mi_plan ids.
     * @return The found projects. If imitsMiPlanId is null then not filter is applied.
     */
    public static Specification<Project> withImitsMiPlans(List<String> imitsMiPlans)
    {
        if (imitsMiPlans == null)
        {
            return null;
        }
        return (root, query, criteriaBuilder) -> {
            Path<Long> imitsMiPlanPath = ProjectPaths.getImitsMiPlanPath(root);
            query.distinct(true);
            return PredicateBuilder.addLowerLikeOrPredicatesId(criteriaBuilder, imitsMiPlanPath, imitsMiPlans);
        };
    }

    /**
     * Get all the projects which plans are related with the outcomes specified in productionOutcomeNames
     *
     * @param productionOutcomeNames List of names of the Outcome Names
     * @return The found projects. If productionOutcomeNames is null then not filter is applied.
     */
    public static Specification<Project> withProductionColonyNames(List<String> productionOutcomeNames)
    {
        if (productionOutcomeNames == null)
        {
            return null;
        }
        return (root, query, criteriaBuilder) -> {
            Path<String> outcomePath = ProjectPaths.getColonyNamePath(root);
            query.distinct(true);
            return PredicateBuilder.addInPredicates(
                    criteriaBuilder, outcomePath, productionOutcomeNames);
        };
    }

    public static Specification<Project> withPhenotypingExternalRefNames(List<String> phenotypingExternalRefNames)
    {
        if (phenotypingExternalRefNames == null)
        {
            return null;
        }
        return (root, query, criteriaBuilder) -> {
            Path<String> phenotypingAttemptPath = ProjectPaths.getPhenotypingExternalRefName(root);
            query.distinct(true);
            return PredicateBuilder.addInPredicates(
                    criteriaBuilder, phenotypingAttemptPath, phenotypingExternalRefNames);
        };
    }

    public static Specification<Project> withoutNullGenesSymbols() {
        return (root, query, criteriaBuilder) -> {
            Path<String> symbolNamePath = ProjectPaths.getMarkerSymbolPath(root);
            query.distinct(true);
            return PredicateBuilder.notInPredicates(
                criteriaBuilder, symbolNamePath);
        };
    }
}
