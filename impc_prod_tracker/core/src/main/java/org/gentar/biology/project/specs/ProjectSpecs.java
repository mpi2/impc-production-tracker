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
import javax.persistence.criteria.Path;
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
        Specification<Project> specification = Specification.where(null);
        if (markerSymbols != null)
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder) -> {
                Path<String> symbolNamePath = ProjectPaths.getMarkerSymbolPath(root);
                query.distinct(true);
                return PredicateBuilder.addInPredicates(
                    criteriaBuilder, symbolNamePath, markerSymbols);
            };
        }
        return specification;
    }

    public static Specification<Project> withGenes(List<String> genesNameOrIds)
    {
        Specification<Project> specification = Specification.where(null);
        if (genesNameOrIds != null)
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder) -> {
                List<String> lowerCaseGenesNameOrIds =
                    genesNameOrIds.stream().map(String::toLowerCase).collect(Collectors.toList());
                Path<String> symbolNamePath = ProjectPaths.getMarkerSymbolPath(root);
                Path<String> accIdPath = ProjectPaths.getAccIdPath(root);

                query.distinct(true);
                return criteriaBuilder.or(
                    criteriaBuilder.lower(symbolNamePath).in(lowerCaseGenesNameOrIds),
                    criteriaBuilder.lower(accIdPath).in(lowerCaseGenesNameOrIds)
                );
            };
        }
        return specification;
    }

    public static Specification<Project> withIntentions(List<String> intentionNames)
    {
        Specification<Project> specification = Specification.where(null);
        if (intentionNames != null)
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder) -> {
                Path<String> geneticMutationTypeNamePath = ProjectPaths.getMolecularMutationTypeNamePath(root);

                query.distinct(true);
                return PredicateBuilder.addInPredicates(
                    criteriaBuilder, geneticMutationTypeNamePath, intentionNames);
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
        Specification<Project> specification = Specification.where(null);
        if (workUnitNames != null)
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder) -> {
                Path<String> workUnitNamePath = ProjectPaths.getWorkUnitNamePath(root);
                query.distinct(true);
                return PredicateBuilder.addInPredicates(
                    criteriaBuilder, workUnitNamePath, workUnitNames);
            };
        }
        return specification;
    }

    /**
     * Get all the projects which plans are related with the work groups specified in workUnitNames
     *
     * @param workGroupNames List of names of the Work Groups
     * @return The found projects. If workUnitNames is null then not filter is applied.
     */
    public static Specification<Project> withPlansInWorkGroupNames(List<String> workGroupNames)
    {
        Specification<Project> specification = Specification.where(null);
        if (workGroupNames != null)
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder) -> {
                Path<String> workGroupNamePath = ProjectPaths.getWorkGroupNamePath(root);
                query.distinct(true);
                return PredicateBuilder.addInPredicates(
                    criteriaBuilder, workGroupNamePath, workGroupNames);
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
        Specification<Project> specification = Specification.where(null);
        if (statuses != null)
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder) -> {
                Path<String> statusNamePath = ProjectPaths.getAssignmentStatusNamePath(root);
                query.distinct(true);
                return PredicateBuilder.addInPredicates(criteriaBuilder, statusNamePath, statuses);
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
        Specification<Project> specification = Specification.where(null);
        if (privacies != null)
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder) -> {
                Path<String> privacyNamePath = ProjectPaths.getPrivacyNamePath(root);
                query.distinct(true);
                return PredicateBuilder.addInPredicates(criteriaBuilder, privacyNamePath, privacies);
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
        Specification<Project> specification = Specification.where(null);
        if (consortia != null)
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder) -> {
                query.distinct(true);
                Path<String> consortiumNamePath = ProjectPaths.getConsortiaNamePath(root);
                return PredicateBuilder.addInPredicates(
                    criteriaBuilder, consortiumNamePath, consortia);
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
        Specification<Project> specification = Specification.where(null);
        if (tpns != null)
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder) -> {
                Path<String> tpnPath = ProjectPaths.getTpnPath(root);
                query.distinct(true);
                return PredicateBuilder.addLowerLikeOrPredicates(criteriaBuilder, tpnPath, tpns);
            };
        }
        return specification;
    }

    public static Specification<Project> withExternalReferences(List<String> externalReferences)
    {
        Specification<Project> specification = Specification.where(null);
        if (externalReferences != null)
        {
            specification = (Specification<Project>) (root, query, criteriaBuilder) -> {
                query.distinct(true);
                Path<String> externalRefPath = ProjectPaths.getExternalReferencePath(root);
                return PredicateBuilder.addLowerLikeOrPredicates(
                    criteriaBuilder, externalRefPath, externalReferences);
            };
        }
        return specification;
    }
}
