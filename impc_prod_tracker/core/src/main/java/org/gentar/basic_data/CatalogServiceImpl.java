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
package org.gentar.basic_data;


import org.gentar.biology.colony.distribution.distribution_network.DistributionNetworkRepository;
import org.gentar.biology.colony.distribution.product_type.ProductTypeRepository;
import org.gentar.biology.plan.attempt.crispr.nuclease.nuclease_class.NucleaseClassRepository;
import org.gentar.biology.plan.attempt.crispr.nuclease.nuclease_type.NucleaseTypeRepository;
import org.gentar.biology.mutation.categorizarion.MutationCategorizationRepository;
import org.gentar.biology.plan.attempt.AttemptTypeRepository;
import org.gentar.biology.sequence.category.SequenceCategoryRepository;
import org.gentar.biology.sequence.type.SequenceTypeRepository;
import org.gentar.organization.consortium.Consortium;
import org.gentar.organization.funder.FunderRepository;
import org.springframework.stereotype.Component;
import org.gentar.biology.mutation.genetic_type.GeneticMutationTypeRepository;
import org.gentar.biology.project.assignment.AssignmentStatusRepository;
import org.gentar.biology.plan.attempt.crispr.mutagenesis_donor.preparation_type.PreparationTypeRepository;
import org.gentar.biology.mutation.molecular_type.MolecularMutationTypeRepository;
import org.gentar.biology.plan.attempt.phenotyping.stage.material_deposited_type.MaterialDepositedTypeRepository;
import org.gentar.biology.plan.type.PlanTypeRepository;
import org.gentar.biology.project.privacy.PrivacyRepository;
import org.gentar.biology.species.SpeciesRepository;
import org.gentar.biology.status.StatusRepository;
import org.gentar.biology.strain.StrainRepository;
import org.gentar.organization.consortium.ConsortiumRepository;
import org.gentar.organization.institute.InstituteRepository;
import org.gentar.organization.work_group.WorkGroupRepository;
import org.gentar.organization.work_unit.WorkUnitRepository;
import org.gentar.biology.project.search.SearchType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CatalogServiceImpl implements CatalogService
{
    private final WorkUnitRepository workUnitRepository;
    private final WorkGroupRepository workGroupRepository;
    private final PlanTypeRepository planTypeRepository;
    private final PrivacyRepository privacyRepository;
    private final StatusRepository statusRepository;
    private final AssignmentStatusRepository assignmentStatusRepository;
    private final GeneticMutationTypeRepository geneticMutationTypeRepository;
    private final InstituteRepository instituteRepository;
    private final StrainRepository strainRepository;
    private final PreparationTypeRepository preparationTypeRepository;
    private final MaterialDepositedTypeRepository materialDepositedTypeRepository;
    private final SpeciesRepository speciesRepository;
    private final ConsortiumRepository consortiumRepository;
    private final MolecularMutationTypeRepository molecularMutationTypeRepository;
    private final NucleaseTypeRepository nucleaseTypeRepository;
    private final NucleaseClassRepository nucleaseClassRepository;
    private final MutationCategorizationRepository mutationCategorizationRepository;
    private final FunderRepository funderRepository;
    private final AttemptTypeRepository attemptTypeRepository;
    private final SequenceTypeRepository sequenceTypeRepository;
    private final SequenceCategoryRepository sequenceCategoryRepository;
    private final ProductTypeRepository productTypeRepository;
    private final DistributionNetworkRepository distributionNetworkRepository;

    private Map<String, List<String>> conf = new HashMap<>();

    public CatalogServiceImpl(
        WorkUnitRepository workUnitRepository,
        WorkGroupRepository workGroupRepository,
        PlanTypeRepository planTypeRepository,
        PrivacyRepository privacyRepository,
        StatusRepository statusRepository,
        AssignmentStatusRepository assignmentStatusRepository,
        GeneticMutationTypeRepository geneticMutationTypeRepository,
        InstituteRepository instituteRepository,
        StrainRepository strainRepository,
        PreparationTypeRepository preparationTypeRepository,
        MaterialDepositedTypeRepository materialDepositedTypeRepository,
        SpeciesRepository speciesRepository,
        ConsortiumRepository consortiumRepository,
        MolecularMutationTypeRepository molecularMutationTypeRepository,
        NucleaseTypeRepository nucleaseTypeRepository,
        NucleaseClassRepository nucleaseClassRepository,
        MutationCategorizationRepository mutationCategorizationRepository,
        FunderRepository funderRepository,
        AttemptTypeRepository attemptTypeRepository,
        SequenceTypeRepository sequenceTypeRepository,
        SequenceCategoryRepository sequenceCategoryRepository,
        ProductTypeRepository productTypeRepository,
        DistributionNetworkRepository distributionNetworkRepository)
    {
        this.workUnitRepository = workUnitRepository;
        this.workGroupRepository = workGroupRepository;
        this.planTypeRepository = planTypeRepository;
        this.privacyRepository = privacyRepository;
        this.statusRepository = statusRepository;
        this.assignmentStatusRepository = assignmentStatusRepository;
        this.geneticMutationTypeRepository = geneticMutationTypeRepository;
        this.instituteRepository = instituteRepository;
        this.strainRepository = strainRepository;
        this.preparationTypeRepository = preparationTypeRepository;
        this.materialDepositedTypeRepository = materialDepositedTypeRepository;
        this.speciesRepository = speciesRepository;
        this.consortiumRepository = consortiumRepository;
        this.molecularMutationTypeRepository = molecularMutationTypeRepository;
        this.nucleaseTypeRepository = nucleaseTypeRepository;
        this.nucleaseClassRepository = nucleaseClassRepository;
        this.mutationCategorizationRepository = mutationCategorizationRepository;
        this.funderRepository = funderRepository;
        this.attemptTypeRepository = attemptTypeRepository;
        this.sequenceTypeRepository = sequenceTypeRepository;
        this.sequenceCategoryRepository = sequenceCategoryRepository;
        this.productTypeRepository = productTypeRepository;
        this.distributionNetworkRepository = distributionNetworkRepository;
    }

    @Override
    public Map<String, List<String>> getCatalog()
    {
        if (conf.isEmpty())
        {
            addWorkUnits();
            addWorkGroups();
            addConsortia();
            addFunders();
            addPlanTypes();
            addPrivacies();
            addStatuses();
            addAssignmentStatuses();
            addGeneticMutationTypes();
            addInstitutes();
            addStrains();
            addMaterialTypes();
            addPreparationTypes();
            addSearchTypes();
            addSpecies();
            addMolecularMutationTypes();
            addNucleaseTypes();
            addNucleaseClasses();
            addMutationCategorizations();
            addAttemptTypes();
            addSequenceTypes();
            addSequenceCategorization();
            addProductTypes();
            addDistributionNetworks();
            addConsortiaToConstructSymbols();
        }
        return conf;
    }

    private void addAttemptTypes()
    {
        List<String> attemptTypes = new ArrayList<>();
        attemptTypeRepository.findAll().forEach(p -> attemptTypes.add(p.getName()));
        conf.put("attemptTypes", attemptTypes);
    }

    private void addFunders()
    {
        List<String> funders = new ArrayList<>();
        funderRepository.findAll().forEach(p -> funders.add(p.getName()));
        conf.put("funders", funders);
    }

    private void addConsortia()
    {
        List<String> consortia = new ArrayList<>();
        consortiumRepository.findAll().forEach(p -> consortia.add(p.getName()));
        conf.put("consortia", consortia);
    }

    private void addWorkUnits()
    {
        List<String> workUnits = new ArrayList<>();
        workUnitRepository.findAll().forEach(p -> workUnits.add(p.getName()));
        conf.put("workUnits", workUnits);
    }

    private void addWorkGroups()
    {
        List<String> workGroups = new ArrayList<>();
        workGroupRepository.findAll().forEach(p -> workGroups.add(p.getName()));
        conf.put("workGroups", workGroups);
    }

    private void addPlanTypes()
    {
        List<String> planTypes = new ArrayList<>();
        planTypeRepository.findAll().forEach(p -> planTypes.add(p.getName()));
        conf.put("planTypes", planTypes);
    }

    private void addPrivacies()
    {
        List<String> privacies = new ArrayList<>();
        privacyRepository.findAll().forEach(p -> privacies.add(p.getName()));
        conf.put("privacies", privacies);
    }

    private void addStatuses()
    {
        List<String> statuses = new ArrayList<>();
        statusRepository.findAll().forEach(p -> statuses.add(p.getName()));
        conf.put("statuses", statuses);
    }

    private void addAssignmentStatuses()
    {
        List<String> assignmentStatuses = new ArrayList<>();
        assignmentStatusRepository.findAll().forEach(p -> assignmentStatuses.add(p.getName()));
        conf.put("assignmentStatuses", assignmentStatuses);
    }

    private void addGeneticMutationTypes()
    {
        List<String> geneticMutationTypes = new ArrayList<>();
        geneticMutationTypeRepository.findAll().forEach(p -> geneticMutationTypes.add(p.getName()));
        conf.put("geneticMutationTypes", geneticMutationTypes);
    }

    private void addInstitutes()
    {
        List<String> institutes = new ArrayList<>();
        instituteRepository.findAll().forEach(p -> institutes.add(p.getName()));
        conf.put("institutes", institutes);
    }

    private void addStrains()
    {
        List<String> trackedStrains = new ArrayList<>();
        strainRepository.findAll().forEach(p -> trackedStrains.add(p.getName()));
        conf.put("trackedStrains", trackedStrains);
    }

    private void addMaterialTypes()
    {
        List<String> materialTypes = new ArrayList<>();
        materialDepositedTypeRepository.findAll().forEach(p -> materialTypes.add(p.getName()));
        conf.put("materialTypes", materialTypes);
    }

    private void addPreparationTypes()
    {
        List<String> preparationTypes = new ArrayList<>();
        preparationTypeRepository.findAll().forEach(p -> preparationTypes.add(p.getName()));
        conf.put("preparationTypes", preparationTypes);
    }

    private void addSearchTypes()
    {
        List<String> searchTypes = SearchType.getValidValuesNames();
        conf.put("searchTypes", searchTypes);
    }

    private void addSpecies()
    {
        List<String> species = new ArrayList<>();
        speciesRepository.findAll().forEach(p -> species.add(p.getName()));
        conf.put("species", species);
    }

    private void addMolecularMutationTypes()
    {
        List<String> molecularMutationTypes = new ArrayList<>();
        molecularMutationTypeRepository.findAll().forEach(p -> molecularMutationTypes.add(p.getName()));
        conf.put("molecularMutationTypes", molecularMutationTypes);
    }

    private void addNucleaseTypes()
    {
        List<String> nucleaseTypes = new ArrayList<>();
        nucleaseTypeRepository.findAll().forEach(p -> nucleaseTypes.add(p.getName()));
        conf.put("nucleaseTypes", nucleaseTypes);
    }

    private void addNucleaseClasses()
    {
        List<String> nucleaseClasses = new ArrayList<>();
        nucleaseClassRepository.findAll().forEach(p -> nucleaseClasses.add(p.getName()));
        conf.put("nucleaseClasses", nucleaseClasses);
    }

    private void addMutationCategorizations ()
    {
        List<String> mutationCategorizations = new ArrayList<>();
        mutationCategorizationRepository.findAll().forEach(p -> mutationCategorizations.add(p.getName()));
        conf.put("mutationCategorizations", mutationCategorizations);
    }

    private void addSequenceTypes ()
    {
        List<String> sequenceTypes = new ArrayList<>();
        sequenceTypeRepository.findAll().forEach(p -> sequenceTypes.add(p.getName()));
        conf.put("sequenceTypes", sequenceTypes);
    }

    private void addSequenceCategorization ()
    {
        List<String> sequenceCategorizations = new ArrayList<>();
        sequenceCategoryRepository.findAll().forEach(p -> sequenceCategorizations.add(p.getName()));
        conf.put("sequenceCategorizations", sequenceCategorizations);
    }

    private void addProductTypes()
    {
        List<String> productTypes = new ArrayList<>();
        productTypeRepository.findAll().forEach(p -> productTypes.add(p.getName()));
        conf.put("productTypes", productTypes);
    }

    private void addDistributionNetworks()
    {
        List<String> distributionNetworks = new ArrayList<>();
        distributionNetworkRepository.findAll().forEach(p -> distributionNetworks.add(p.getName()));
        conf.put("distributionNetworks", distributionNetworks);
    }

    /**
     * In the future this list should be created from a query in the database. Currently it's
     * fixed and only have IMPC as consortium whom abbreviation can be used to construct the
     * symbol of an allele.
     */
    private void addConsortiaToConstructSymbols()
    {
        List<String> consortiaToConstructSymbols = new ArrayList<>();
        // Make sure the consortium exists in database
        Consortium impc = consortiumRepository.findByNameIgnoreCase("IMPC");
        consortiaToConstructSymbols.add(impc.getName());
        conf.put("consortiaToConstructSymbols", consortiaToConstructSymbols);
    }
}
