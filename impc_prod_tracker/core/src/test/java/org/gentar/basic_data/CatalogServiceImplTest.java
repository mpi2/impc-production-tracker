package org.gentar.basic_data;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.gentar.biology.colony.distribution.distribution_network.DistributionNetworkRepository;
import org.gentar.biology.colony.distribution.product_type.ProductTypeRepository;
import org.gentar.biology.gene_list.record.ListRecordTypeService;
import org.gentar.biology.mutation.categorizarion.MutationCategorizationRepository;
import org.gentar.biology.mutation.categorizarion.MutationCategorizationService;
import org.gentar.biology.mutation.genetic_type.GeneticMutationTypeRepository;
import org.gentar.biology.mutation.molecular_type.MolecularMutationTypeRepository;
import org.gentar.biology.mutation.qc_results.QcStatusRepository;
import org.gentar.biology.mutation.qc_results.QcTypeRepository;
import org.gentar.biology.outcome.type.OutcomeTypeRepository;
import org.gentar.biology.plan.PlanSpecs;
import org.gentar.biology.plan.attempt.AttemptTypeService;
import org.gentar.biology.plan.attempt.crispr.assay.AssayTypeRepository;
import org.gentar.biology.plan.attempt.crispr.guide.GuideFormatRepository;
import org.gentar.biology.plan.attempt.crispr.guide.GuideSourceRepository;
import org.gentar.biology.plan.attempt.crispr.mutagenesis_donor.preparation_type.PreparationTypeRepository;
import org.gentar.biology.plan.attempt.crispr.nuclease.nuclease_class.NucleaseClassRepository;
import org.gentar.biology.plan.attempt.crispr.nuclease.nuclease_type.NucleaseTypeRepository;
import org.gentar.biology.plan.attempt.crispr.reagent.ReagentRepository;
import org.gentar.biology.plan.attempt.phenotyping.stage.material_deposited_type.MaterialDepositedTypeRepository;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageTypeService;
import org.gentar.biology.plan.type.PlanTypeRepository;
import org.gentar.biology.project.assignment.AssignmentStatusRepository;
import org.gentar.biology.project.completionNote.ProjectCompletionNoteRepository;
import org.gentar.biology.project.esCellQc.centre_pipeline.EsCellCentrePipelineRepository;
import org.gentar.biology.project.esCellQc.comment.EsCellQcCommentRepository;
import org.gentar.biology.project.privacy.PrivacyRepository;
import org.gentar.biology.sequence.category.SequenceCategoryRepository;
import org.gentar.biology.sequence.type.SequenceTypeRepository;
import org.gentar.biology.species.SpeciesRepository;
import org.gentar.biology.status.StatusRepository;
import org.gentar.biology.strain.StrainRepository;
import org.gentar.biology.strain.strain_type.StrainTypeRepository;
import org.gentar.organization.consortium.ConsortiumService;
import org.gentar.organization.funder.FunderService;
import org.gentar.organization.institute.InstituteRepository;
import org.gentar.organization.work_group.WorkGroupService;
import org.gentar.organization.work_unit.WorkUnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CatalogServiceImplTest {


    @Mock
    private AttemptTypeService attemptTypeService;
    @Mock
    private WorkUnitRepository workUnitRepository;
    @Mock
    private WorkGroupService workGroupService;
    @Mock
    private PlanTypeRepository planTypeRepository;
    @Mock
    private PrivacyRepository privacyRepository;
    @Mock
    private StatusRepository statusRepository;
    @Mock
    private AssignmentStatusRepository assignmentStatusRepository;
    @Mock
    private GeneticMutationTypeRepository geneticMutationTypeRepository;
    @Mock
    private InstituteRepository instituteRepository;
    @Mock
    private StrainRepository strainRepository;
    @Mock
    private StrainTypeRepository strainTypeRepository;
    @Mock
    private PreparationTypeRepository preparationTypeRepository;
    @Mock
    private MaterialDepositedTypeRepository materialDepositedTypeRepository;
    @Mock
    private SpeciesRepository speciesRepository;
    @Mock
    private ConsortiumService consortiumService;
    @Mock
    private MolecularMutationTypeRepository molecularMutationTypeRepository;
    @Mock
    private NucleaseTypeRepository nucleaseTypeRepository;
    @Mock
    private NucleaseClassRepository nucleaseClassRepository;
    @Mock
    private MutationCategorizationRepository mutationCategorizationRepository;
    @Mock
    private FunderService funderService;
    @Mock
    private OutcomeTypeRepository outcomeTypeRepository;
    @Mock
    private SequenceTypeRepository sequenceTypeRepository;
    @Mock
    private SequenceCategoryRepository sequenceCategoryRepository;
    @Mock
    private ProductTypeRepository productTypeRepository;
    @Mock
    private DistributionNetworkRepository distributionNetworkRepository;
    @Mock
    private QcTypeRepository qcTypeRepository;
    @Mock
    private QcStatusRepository qcStatusRepository;
    @Mock
    private ReagentRepository reagentRepository;
    @Mock
    private AssayTypeRepository assayTypeRepository;
    @Mock
    private PhenotypingStageTypeService phenotypingStageTypeService;
    @Mock
    private MutationCategorizationService mutationCategorizationService;
    @Mock
    private ListRecordTypeService listRecordTypeService;
    @Mock
    private GuideFormatRepository guideFormatRepository;
    @Mock
    private GuideSourceRepository guideSourceRepository;
    @Mock
    private EsCellCentrePipelineRepository esCellCentrePipelineRepository;
    @Mock
    private EsCellQcCommentRepository esCellQcCommentRepository;
    @Mock
    private ProjectCompletionNoteRepository projectCompletionNoteRepository;

    CatalogServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new CatalogServiceImpl(attemptTypeService,
            workUnitRepository,
            workGroupService,
            planTypeRepository,
            privacyRepository,
            statusRepository,
            assignmentStatusRepository,
            geneticMutationTypeRepository,
            instituteRepository,
            strainRepository,
            strainTypeRepository,
            preparationTypeRepository,
            materialDepositedTypeRepository,
            speciesRepository,
            consortiumService,
            molecularMutationTypeRepository,
            nucleaseTypeRepository,
            nucleaseClassRepository,
            mutationCategorizationRepository,
            funderService,
            outcomeTypeRepository,
            sequenceTypeRepository,
            sequenceCategoryRepository,
            productTypeRepository,
            distributionNetworkRepository,
            qcTypeRepository,
            qcStatusRepository,
            reagentRepository,
            assayTypeRepository,
            phenotypingStageTypeService,
            mutationCategorizationService,
            listRecordTypeService,
            guideFormatRepository,
            guideSourceRepository,
            esCellCentrePipelineRepository,
            esCellQcCommentRepository,
            projectCompletionNoteRepository);
    }

    @Test
    void getCatalog() {
        Map<String, Object> conf=testInstance.getCatalog();
        assertEquals(conf.size(),46);
    }
}