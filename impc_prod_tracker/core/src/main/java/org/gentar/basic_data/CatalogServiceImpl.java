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
import org.gentar.biology.gene_list.record.ListRecordTypeService;
import org.gentar.biology.mutation.categorizarion.MutationCategorizationService;
import org.gentar.biology.mutation.qc_results.QcStatusRepository;
import org.gentar.biology.mutation.qc_results.QcTypeRepository;
import org.gentar.biology.outcome.type.OutcomeTypeRepository;
import org.gentar.biology.plan.attempt.AttemptTypeService;
import org.gentar.biology.plan.attempt.crispr.assay.AssayTypeRepository;
import org.gentar.biology.plan.attempt.crispr.guide.GuideFormatRepository;
import org.gentar.biology.plan.attempt.crispr.guide.GuideSourceRepository;
import org.gentar.biology.plan.attempt.crispr.nuclease.nuclease_class.NucleaseClassRepository;
import org.gentar.biology.plan.attempt.crispr.nuclease.nuclease_type.NucleaseTypeRepository;
import org.gentar.biology.mutation.categorizarion.MutationCategorizationRepository;
import org.gentar.biology.plan.attempt.crispr.reagent.ReagentRepository;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageTypeService;
import org.gentar.biology.sequence.category.SequenceCategoryRepository;
import org.gentar.biology.sequence.type.SequenceTypeRepository;
import org.gentar.biology.strain.strain_type.StrainType;
import org.gentar.biology.strain.strain_type.StrainTypeRepository;
import org.gentar.organization.consortium.ConsortiumService;
import org.gentar.organization.funder.FunderService;
import org.gentar.organization.work_group.WorkGroupService;
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
import org.gentar.organization.institute.InstituteRepository;
import org.gentar.organization.work_unit.WorkUnitRepository;
import org.gentar.biology.project.search.SearchType;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CatalogServiceImpl implements CatalogService
{
    private final AttemptTypeService attemptTypeService;
    private final WorkUnitRepository workUnitRepository;
    private final WorkGroupService workGroupService;
    private final PlanTypeRepository planTypeRepository;
    private final PrivacyRepository privacyRepository;
    private final StatusRepository statusRepository;
    private final AssignmentStatusRepository assignmentStatusRepository;
    private final GeneticMutationTypeRepository geneticMutationTypeRepository;
    private final InstituteRepository instituteRepository;
    private final StrainRepository strainRepository;
    private final StrainTypeRepository strainTypeRepository;
    private final PreparationTypeRepository preparationTypeRepository;
    private final MaterialDepositedTypeRepository materialDepositedTypeRepository;
    private final SpeciesRepository speciesRepository;
    private final ConsortiumService consortiumService;
    private final MolecularMutationTypeRepository molecularMutationTypeRepository;
    private final NucleaseTypeRepository nucleaseTypeRepository;
    private final NucleaseClassRepository nucleaseClassRepository;
    private final MutationCategorizationRepository mutationCategorizationRepository;
    private final FunderService funderService;
    private final OutcomeTypeRepository outcomeTypeRepository;
    private final SequenceTypeRepository sequenceTypeRepository;
    private final SequenceCategoryRepository sequenceCategoryRepository;
    private final ProductTypeRepository productTypeRepository;
    private final DistributionNetworkRepository distributionNetworkRepository;
    private final QcTypeRepository qcTypeRepository;
    private final QcStatusRepository qcStatusRepository;
    private final ReagentRepository reagentRepository;
    private final AssayTypeRepository assayTypeRepository;
    private final PhenotypingStageTypeService phenotypingStageTypeService;
    private final MutationCategorizationService mutationCategorizationService;
    private final ListRecordTypeService listRecordTypeService;
    private final GuideFormatRepository guideFormatRepository;
    private final GuideSourceRepository guideSourceRepository;

    private final Map<String, Object> conf = new HashMap<>();

    public CatalogServiceImpl(
            AttemptTypeService attemptTypeService,
            WorkUnitRepository workUnitRepository,
            WorkGroupService workGroupService, PlanTypeRepository planTypeRepository,
            PrivacyRepository privacyRepository,
            StatusRepository statusRepository,
            AssignmentStatusRepository assignmentStatusRepository,
            GeneticMutationTypeRepository geneticMutationTypeRepository,
            InstituteRepository instituteRepository,
            StrainRepository strainRepository,
            StrainTypeRepository strainTypeRepository,
            PreparationTypeRepository preparationTypeRepository,
            MaterialDepositedTypeRepository materialDepositedTypeRepository,
            SpeciesRepository speciesRepository,
            ConsortiumService consortiumService,
            MolecularMutationTypeRepository molecularMutationTypeRepository,
            NucleaseTypeRepository nucleaseTypeRepository,
            NucleaseClassRepository nucleaseClassRepository,
            MutationCategorizationRepository mutationCategorizationRepository,
            FunderService funderService, OutcomeTypeRepository outcomeTypeRepository,
            SequenceTypeRepository sequenceTypeRepository,
            SequenceCategoryRepository sequenceCategoryRepository,
            ProductTypeRepository productTypeRepository,
            DistributionNetworkRepository distributionNetworkRepository,
            QcTypeRepository qcTypeRepository,
            QcStatusRepository qcStatusRepository,
            ReagentRepository reagentRepository,
            AssayTypeRepository assayTypeRepository,
            PhenotypingStageTypeService phenotypingStageTypeService,
            MutationCategorizationService mutationCategorizationService,
            ListRecordTypeService listRecordTypeService, GuideFormatRepository guideFormatRepository, GuideSourceRepository guideSourceRepository)
    {
        this.attemptTypeService = attemptTypeService;
        this.workUnitRepository = workUnitRepository;
        this.workGroupService = workGroupService;
        this.planTypeRepository = planTypeRepository;
        this.privacyRepository = privacyRepository;
        this.statusRepository = statusRepository;
        this.assignmentStatusRepository = assignmentStatusRepository;
        this.geneticMutationTypeRepository = geneticMutationTypeRepository;
        this.instituteRepository = instituteRepository;
        this.strainRepository = strainRepository;
        this.strainTypeRepository = strainTypeRepository;
        this.preparationTypeRepository = preparationTypeRepository;
        this.materialDepositedTypeRepository = materialDepositedTypeRepository;
        this.speciesRepository = speciesRepository;
        this.consortiumService = consortiumService;
        this.molecularMutationTypeRepository = molecularMutationTypeRepository;
        this.nucleaseTypeRepository = nucleaseTypeRepository;
        this.nucleaseClassRepository = nucleaseClassRepository;
        this.mutationCategorizationRepository = mutationCategorizationRepository;
        this.funderService = funderService;
        this.outcomeTypeRepository = outcomeTypeRepository;
        this.sequenceTypeRepository = sequenceTypeRepository;
        this.sequenceCategoryRepository = sequenceCategoryRepository;
        this.productTypeRepository = productTypeRepository;
        this.distributionNetworkRepository = distributionNetworkRepository;
        this.qcTypeRepository = qcTypeRepository;
        this.qcStatusRepository = qcStatusRepository;
        this.reagentRepository = reagentRepository;
        this.assayTypeRepository = assayTypeRepository;
        this.phenotypingStageTypeService = phenotypingStageTypeService;
        this.mutationCategorizationService = mutationCategorizationService;
        this.listRecordTypeService = listRecordTypeService;
        this.guideFormatRepository = guideFormatRepository;
        this.guideSourceRepository = guideSourceRepository;
    }

    @Override
    public Map<String, Object> getCatalog()
    {
        if (conf.isEmpty())
        {
            addAttemptTypesByPlanTypes();
            addWorkGroupsByWorkUnits();
            addWorkUnits();
            addWorkGroups();
            addConsortia();
            addFunders();
            addFundersByWorkGroups();
            addPlanTypes();
            addPrivacies();
            addStatuses();
            addAssignmentStatuses();
            addGeneticMutationTypes();
            addInstitutes();
            addStrains();
            addBackGroundStrains();
            addDeleterStrains();
            addTestCrossStrains();
            addBlastStrains();
            addMaterialTypes();
            addPreparationTypes();
            addSearchTypes();
            addSpecies();
            addMolecularMutationTypes();
            addMutationCategorizationsByType();
            addNucleaseTypes();
            addNucleaseClasses();
            addMutationCategorizations();
            addAttemptTypes();
            addSequenceTypes();
            addSequenceCategorization();
            addProductTypes();
            addOutcomeTypes();
            addDistributionNetworks();
            addConsortiaToConstructSymbols();
            addQcTypes();
            addQcStatuses();
            addReagents();
            addAssayTypes();
            addPhenotypingStagesTypes();
            addPhenotypingStagesTypesByAttemptTypes();
            addRecordTypesByConsortium();
            addGuideFormatNames();
            addGuideSourceNames();
        }
        return conf;
    }

    private void addGuideFormatNames()
    {
        List<Object> guideFormatNames = new ArrayList<>();
        guideFormatRepository.findAll().forEach(p -> guideFormatNames.add(p.getName()));
        conf.put("guideFormatNames", guideFormatNames);
    }

    private void addGuideSourceNames()
    {
        List<Object> guideSourceNames = new ArrayList<>();
        guideSourceRepository.findAll().forEach(p -> guideSourceNames.add(p.getName()));
        conf.put("guideSourceNames", guideSourceNames);
    }

    private void addAttemptTypesByPlanTypes()
    {
        Map<String, List<String>> map = attemptTypeService.getAttemptTypesByPlanTypeNameMap();
        conf.put("attemptTypesByPlanTypes", map);
    }

    private void addWorkGroupsByWorkUnits()
    {
        Map<String, List<String>> map = workGroupService.getWorkGroupsNamesByWorkUnitNamesMap();
        conf.put("workGroupsByWorkUnits", map);
    }

    private void addAttemptTypes()
    {
        List<Object> attemptTypes = new ArrayList<>();
        attemptTypeService.getAll().forEach(p -> attemptTypes.add(p.getName()));
        conf.put("attemptTypes", attemptTypes);
    }

    private void addFunders()
    {
        List<Object> funders = new ArrayList<>();
        funderService.getAll().forEach(p -> funders.add(p.getName()));
        conf.put("funders", funders);
    }

    private void addFundersByWorkGroups()
    {
        Map<String, List<String>> map = funderService.getFunderNamesByWorkGroupNamesMap();
        conf.put("fundersByWorkGroups", map);
    }

    private void addConsortia()
    {
        List<Object> consortia = new ArrayList<>();
        consortiumService.findAllConsortia().forEach(p -> consortia.add(p.getName()));
        conf.put("consortia", consortia);
    }

    private void addWorkUnits()
    {
        List<Object> workUnits = new ArrayList<>();
        workUnitRepository.findAll().forEach(p -> workUnits.add(p.getName()));
        conf.put("workUnits", workUnits);
    }

    private void addWorkGroups()
    {
        List<Object> workGroups = new ArrayList<>();
        workGroupService.getAll().forEach(p -> workGroups.add(p.getName()));
        conf.put("workGroups", workGroups);
    }

    private void addPlanTypes()
    {
        List<Object> planTypes = new ArrayList<>();
        planTypeRepository.findAll().forEach(p -> planTypes.add(p.getName()));
        conf.put("planTypes", planTypes);
    }

    private void addPrivacies()
    {
        List<Object> privacies = new ArrayList<>();
        privacyRepository.findAll().forEach(p -> privacies.add(p.getName()));
        conf.put("privacies", privacies);
    }

    private void addStatuses()
    {
        List<Object> statuses = new ArrayList<>();
        statusRepository.findAll().forEach(p -> statuses.add(p.getName()));
        List<Object> sortedStatuses = statuses
                .stream()
                .sorted(Comparator.comparing(Object::toString))
                .collect(Collectors.toList());
        conf.put("statuses", sortedStatuses);
    }

    private void addAssignmentStatuses()
    {
        List<Object> assignmentStatuses = new ArrayList<>();
        assignmentStatusRepository.findAll().forEach(p -> assignmentStatuses.add(p.getName()));
        conf.put("assignmentStatuses", assignmentStatuses);
    }

    private void addGeneticMutationTypes()
    {
        List<Object> geneticMutationTypes = new ArrayList<>();
        geneticMutationTypeRepository.findAll().forEach(p -> geneticMutationTypes.add(p.getName()));
        conf.put("geneticMutationTypes", geneticMutationTypes);
    }

    private void addInstitutes()
    {
        List<Object> institutes = new ArrayList<>();
        instituteRepository.findAll().forEach(p -> institutes.add(p.getName()));
        conf.put("institutes", institutes);
    }

    private void addStrains()
    {
        List<Object> trackedStrains = new ArrayList<>();
        strainRepository.findAll().forEach(p -> trackedStrains.add(p.getName()));
        conf.put("trackedStrains", trackedStrains);
    }

    private void addBackGroundStrains()
    {
        List<Object> backgroundStrains = new ArrayList<>();
        StrainType backgroundStrainType = strainTypeRepository.findByName("background strain");
        strainRepository.findAllByStrainTypesIn(Collections.singletonList(backgroundStrainType))
            .forEach(p -> backgroundStrains.add(p.getName()));
        conf.put("backgroundStrains", backgroundStrains);
    }

    private void addDeleterStrains()
    {
        List<Object> deleterStrains = new ArrayList<>();
        StrainType deleterStrainType = strainTypeRepository.findByName("deleter");
        strainRepository.findAllByStrainTypesIn(Collections.singletonList(deleterStrainType))
                .forEach(p -> deleterStrains.add(p.getName()));
        conf.put("deleterStrains", deleterStrains);
    }

    private void addTestCrossStrains()
    {
        List<Object> testCrossStrains = new ArrayList<>();
        StrainType testCrossStrainType = strainTypeRepository.findByName("test cross strain");
        strainRepository.findAllByStrainTypesIn(Collections.singletonList(testCrossStrainType))
                .forEach(p -> testCrossStrains.add(p.getName()));
        conf.put("testCrossStrains", testCrossStrains);
    }

    private void addBlastStrains()
    {
        List<Object> blastStrains = new ArrayList<>();
        StrainType blastStrainType = strainTypeRepository.findByName("blast strain");
        strainRepository.findAllByStrainTypesIn(Collections.singletonList(blastStrainType))
                .forEach(p -> blastStrains.add(p.getName()));
        conf.put("blastStrains", blastStrains);
    }

    private void addMaterialTypes()
    {
        List<Object> materialTypes = new ArrayList<>();
        materialDepositedTypeRepository.findAll().forEach(p -> materialTypes.add(p.getName()));
        conf.put("materialTypes", materialTypes);
    }

    private void addPreparationTypes()
    {
        List<Object> preparationTypes = new ArrayList<>();
        preparationTypeRepository.findAll().forEach(p -> preparationTypes.add(p.getName()));
        conf.put("preparationTypes", preparationTypes);
    }

    private void addSearchTypes()
    {
        List<Object> searchTypes = new ArrayList<>();
        var res = SearchType.getValidValuesNames();
        searchTypes.addAll(res);
        conf.put("searchTypes", searchTypes);
    }

    private void addSpecies()
    {
        List<Object> species = new ArrayList<>();
        speciesRepository.findAll().forEach(p -> species.add(p.getName()));
        conf.put("species", species);
    }

    private void addMolecularMutationTypes()
    {
        List<Object> molecularMutationTypes = new ArrayList<>();
        molecularMutationTypeRepository.findAll().forEach(p -> molecularMutationTypes.add(p.getName()));
        conf.put("molecularMutationTypes", molecularMutationTypes);
    }

    private void addNucleaseTypes()
    {
        List<Object> nucleaseTypes = new ArrayList<>();
        nucleaseTypeRepository.findAll().forEach(p -> nucleaseTypes.add(p.getName()));
        conf.put("nucleaseTypes", nucleaseTypes);
    }

    private void addNucleaseClasses()
    {
        List<Object> nucleaseClasses = new ArrayList<>();
        nucleaseClassRepository.findAll().forEach(p -> nucleaseClasses.add(p.getName()));
        conf.put("nucleaseClasses", nucleaseClasses);
    }

    private void addMutationCategorizations ()
    {
        List<Object> mutationCategorizations = new ArrayList<>();
        mutationCategorizationRepository.findAll().forEach(p -> mutationCategorizations.add(p.getName()));
        conf.put("mutationCategorizations", mutationCategorizations);
    }

    private void addMutationCategorizationsByType()
    {
        Map<String, List<String>> map =
            mutationCategorizationService.getMutationCategorizationNamesByCategorizationTypesNames();
        conf.put("mutationCategorizationsByType", map);
    }

    private void addSequenceTypes ()
    {
        List<Object> sequenceTypes = new ArrayList<>();
        sequenceTypeRepository.findAll().forEach(p -> sequenceTypes.add(p.getName()));
        conf.put("sequenceTypes", sequenceTypes);
    }

    private void addSequenceCategorization ()
    {
        List<Object> sequenceCategorizations = new ArrayList<>();
        sequenceCategoryRepository.findAll().forEach(p -> sequenceCategorizations.add(p.getName()));
        conf.put("sequenceCategorizations", sequenceCategorizations);
    }

    private void addProductTypes()
    {
        List<Object> productTypes = new ArrayList<>();
        productTypeRepository.findAll().forEach(p -> productTypes.add(p.getName()));
        conf.put("productTypes", productTypes);
    }

    private void addOutcomeTypes()
    {
        List<Object> outcomeTypes = new ArrayList<>();
        outcomeTypeRepository.findAll().forEach(p -> outcomeTypes.add(p.getName()));
        conf.put("outcomeTypes", outcomeTypes);
    }

    private void addDistributionNetworks()
    {
        List<Object> distributionNetworks = new ArrayList<>();
        distributionNetworkRepository.findAll().forEach(p -> distributionNetworks.add(p.getName()));
        conf.put("distributionNetworks", distributionNetworks);
    }

    private void addConsortiaToConstructSymbols()
    {
        var consortiaNames = consortiumService.getConsortiaNamesUsableToConstructSymbols();
        List<Object> consortiaToConstructSymbols = new ArrayList<Object>(consortiaNames);
        conf.put("consortiaToConstructSymbols", consortiaToConstructSymbols);
    }

    private void addQcTypes()
    {
        List<Object> qcTypes = new ArrayList<>();
        qcTypeRepository.findAll().forEach(p -> qcTypes.add(p.getName()));
        conf.put("qcTypes", qcTypes);
    }

    private void addQcStatuses()
    {
        List<Object> qcStatuses = new ArrayList<>();
        qcStatusRepository.findAll().forEach(p -> qcStatuses.add(p.getName()));
        conf.put("qcStatuses", qcStatuses);
    }

    private void addReagents()
    {
        List<Object> reagents = new ArrayList<>();
        reagentRepository.findAll().forEach(p -> reagents.add(p.getName()));
        conf.put("reagents", reagents);
    }

    private void addAssayTypes()
    {
        List<Object> assayTypes = new ArrayList<>();
        assayTypeRepository.findAll().forEach(p -> assayTypes.add(p.getName()));
        conf.put("assayTypes", assayTypes);
    }

    private void addPhenotypingStagesTypes()
    {
        List<Object> phenotypingStagesTypes = new ArrayList<>();
        phenotypingStageTypeService.getAll().forEach(p -> phenotypingStagesTypes.add(p.getName()));
        conf.put("phenotypingStagesTypes", phenotypingStagesTypes);
    }

    private void addPhenotypingStagesTypesByAttemptTypes()
    {
        Map<String, List<String>> map =
            phenotypingStageTypeService.getPhenotypingStageTypeNamesByAttemptTypeNamesMap();
        conf.put("phenotypingStagesTypesByAttemptTypes", map);
    }

    private void addRecordTypesByConsortium()
    {
        Map<String, List<String>> map = listRecordTypeService.getRecordTypesByConsortium();
        conf.put("recordTypesByConsortium", map);
    }
}
