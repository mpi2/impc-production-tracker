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
package org.gentar.conf;

import org.gentar.biology.plan.attempt.crispr.nuclease.nuclease_class.NucleaseClassRepository;
import org.gentar.biology.plan.attempt.crispr.nuclease.nuclease_type.NucleaseTypeRepository;
import org.gentar.biology.mutation.categorizarion.MutationCategorizationRepository;
import org.gentar.biology.plan.attempt.AttemptTypeRepository;
import org.gentar.biology.sequence.category.SequenceCategoryRepository;
import org.gentar.biology.sequence.type.SequenceTypeRepository;
import org.gentar.organization.funder.FunderRepository;
import org.springframework.stereotype.Component;
import org.gentar.biology.mutation.genetic_type.GeneticMutationTypeRepository;
import org.gentar.biology.project.assignment_status.AssignmentStatusRepository;
import org.gentar.biology.plan.attempt.crispr.mutagenesis_donor.preparation_type.PreparationTypeRepository;
import org.gentar.biology.mutation.molecular_type.MolecularMutationTypeRepository;
import org.gentar.biology.plan.attempt.phenotyping.material_deposited_type.MaterialDepositedTypeRepository;
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
    private WorkUnitRepository workUnitRepository;
    private WorkGroupRepository workGroupRepository;
    private PlanTypeRepository planTypeRepository;
    private PrivacyRepository privacyRepository;
    private StatusRepository statusRepository;
    private AssignmentStatusRepository assignmentStatusRepository;
    private GeneticMutationTypeRepository geneticMutationTypeRepository;
    private InstituteRepository instituteRepository;
    private StrainRepository strainRepository;
    private PreparationTypeRepository preparationTypeRepository;
    private MaterialDepositedTypeRepository materialDepositedTypeRepository;
    private SpeciesRepository speciesRepository;
    private ConsortiumRepository consortiumRepository;
    private MolecularMutationTypeRepository molecularMutationTypeRepository;
    private NucleaseTypeRepository nucleaseTypeRepository;
    private NucleaseClassRepository nucleaseClassRepository;
    private MutationCategorizationRepository mutationCategorizationRepository;
    private FunderRepository funderRepository;
    private AttemptTypeRepository attemptTypeRepository;
    private SequenceTypeRepository sequenceTypeRepository;
    private SequenceCategoryRepository sequenceCategoryRepository;

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
        SequenceCategoryRepository sequenceCategoryRepository
    )
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
    }

    @Override
    public Map<String, List<String>> getCatalog()
    {
        if (conf.isEmpty())
        {
            addWorkUnits();
            addWorkGroups();
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
            addConsortia();
            addMolecularMutationTypes();
            addNucleaseTypes();
            addNucleaseClasses();
            addAlleleCategorizations();
            addFunders();
            addAttemptTypes();
            addSequenceTypes();
            addSequenceCategorization();
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

    private void addAlleleCategorizations ()
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

}
