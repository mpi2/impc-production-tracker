package org.gentar.biology.mutation;

import static org.gentar.mockdata.MockData.molecularMutationTypeMockData;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.mutation.categorizarion.MutationCategorization;
import org.gentar.biology.mutation.categorizarion.MutationCategorizationService;
import org.gentar.biology.mutation.categorizarion.type.MutationCategorizationType;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.AttemptType;
import org.gentar.biology.plan.attempt.AttemptTypesName;
import org.gentar.biology.sequence.Sequence;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.organization.work_group.WorkGroup;
import org.gentar.organization.work_unit.WorkUnit;
import org.gentar.organization.work_unit.WorkUnitService;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MutationValidatorTest {

    @Mock
    private ContextAwarePolicyEnforcement policyEnforcement;
    @Mock
    private MutationRepository mutationRepository;
    @Mock
    private WorkUnitService workUnitService;
    @Mock
    MutationCategorizationService mutationCategorizationService;

    MutationValidator testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new MutationValidator(policyEnforcement, mutationRepository,
            mutationCategorizationService, workUnitService);
    }

    @Test
    void validateDataGeneEmpty() {
        Mutation mutation = new Mutation();
        Set<Gene> genes = new HashSet<>();
        mutation.setGenes(genes);
        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validateData(mutation));

        String expectedMessage = "Mutation: Gene(s) cannot be null.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateDataMolecularMutationTypeEmpty() {
        Mutation mutation = new Mutation();
        mutation.setGenes(geneSetMockData("Rsph3b"));
        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validateData(mutation));

        String expectedMessage = "Mutation: Molecular Mutation Type(s) cannot be null.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateDataMutationSymbolEmpty() {
        Mutation mutation = new Mutation();
        mutation.setGenes(geneSetMockData("Wtsi"));
        mutation.setMolecularMutationType(molecularMutationTypeMockData());
        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validateData(mutation));

        String expectedMessage = "Mutation: Mutation symbol(s) cannot be null.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateDataMutationTwoGenesExist() {
        Mutation mutation = new Mutation();
        Set<Gene> geneList = new HashSet<>();
        geneList.add(geneMockData("Rsph3b"));
        geneList.add(geneMockData("Rsph3d"));
        mutation.setGenes(geneList);
        mutation.setMolecularMutationType(molecularMutationTypeMockData());
        mutation.setSymbol("Rsph3b<tm1a(IMPC)Wtsi>");

        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validateData(mutation));

        String expectedMessage =
            "Mutation:(Rsph3b<tm1a(IMPC)Wtsi>) Mutation symbol(s) are not in the correct format";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateDataMutationGraterSymbolMissing() {
        Mutation mutation = new Mutation();
        Set<Gene> geneList = new HashSet<>();
        geneList.add(geneMockData("Rsph3b"));
        mutation.setGenes(geneList);
        mutation.setMolecularMutationType(molecularMutationTypeMockData());
        mutation.setSymbol("Rsph3btm1a(IMPC)Wtsi>");

        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validateData(mutation));

        String expectedMessage =
            "Mutation:(Rsph3btm1a(IMPC)Wtsi>) Mutation symbol(s) are not in the correct format";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateDataMutationIlarCode() {
        Mutation mutation = new Mutation();
        Set<Gene> geneList = new HashSet<>();
        geneList.add(geneMockData("Rsph3b"));
        mutation.setGenes(geneList);
        mutation.setMolecularMutationType(molecularMutationTypeMockData());
        mutation.setSymbol("Rsph3b<tm1a(IMPC)Wtsi>");
        mutation.setOutcomes(outcomesSetMockData(AttemptTypesName.ES_CELL.getLabel(), "Wtsi"));
        lenient().when(workUnitService.getAllWorkUnits())
            .thenReturn(List.of(workUnitMockData("KMPC")));
        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validateData(mutation));

        String expectedMessage =
            "Mutation:(Rsph3b<tm1a(IMPC)Wtsi>) Mutation symbol(s) are not in the correct format";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateDataMutationGeneDoesNotMatch() {
        Mutation mutation = new Mutation();
        Set<Gene> geneList = new HashSet<>();
        geneList.add(geneMockData("Rsph3d"));
        mutation.setGenes(geneList);
        mutation.setMolecularMutationType(molecularMutationTypeMockData());
        mutation.setSymbol("Rsph3b<tm1a(IMPC)Wtsi>");
        mutation.setOutcomes(outcomesSetMockData(AttemptTypesName.ES_CELL.getLabel(), "Wtsi"));

        lenient().when(workUnitService.getAllWorkUnits())
            .thenReturn(List.of(workUnitMockData("Wtsi")));

        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validateData(mutation));

        String expectedMessage =
            "Mutation:(Rsph3b<tm1a(IMPC)Wtsi>) Mutation symbol(s) are not in the correct format";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void validateDataMutationGeneEsCellAndNotTM() {
        Mutation mutation = new Mutation();
        Set<Gene> geneList = new HashSet<>();
        geneList.add(geneMockData("Rsph3b"));
        mutation.setGenes(geneList);
        mutation.setMolecularMutationType(molecularMutationTypeMockData());
        mutation.setSymbol("Rsph3b<em1a(IMPC)Wtsi>");
        mutation.setOutcomes(outcomesSetMockData(AttemptTypesName.ES_CELL.getLabel(), "Wtsi"));

        lenient().when(workUnitService.getAllWorkUnits())
            .thenReturn(List.of(workUnitMockData("Wtsi")));

        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validateData(mutation));

        String expectedMessage =
            "Mutation:(Rsph3b<em1a(IMPC)Wtsi>) Mutation symbol(s) are not in the correct format";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void validateDataMutationGeneCrspirAndNotEM() {
        Mutation mutation = new Mutation();
        Set<Gene> geneList = new HashSet<>();
        geneList.add(geneMockData("Rsph3b"));
        mutation.setGenes(geneList);
        mutation.setMolecularMutationType(molecularMutationTypeMockData());
        mutation.setSymbol("Rsph3b<tm1a(IMPC)Wtsi>");
        mutation.setOutcomes(outcomesSetMockData(AttemptTypesName.CRISPR.getLabel(), "Wtsi"));

        lenient().when(workUnitService.getAllWorkUnits())
            .thenReturn(List.of(workUnitMockData("Wtsi")));

        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validateData(mutation));

        String expectedMessage =
            "Mutation:(Rsph3b<tm1a(IMPC)Wtsi>) Mutation symbol(s) are not in the correct format";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateDataMutationGeneNextIdWrong() {
        Mutation mutation = new Mutation();
        Set<Gene> geneList = new HashSet<>();
        geneList.add(geneMockData("Rsph3b"));
        mutation.setGenes(geneList);
        mutation.setMolecularMutationType(molecularMutationTypeMockData());
        mutation.setSymbol("Rsph3b<tma(IMPC)Wtsi>");
        mutation.setOutcomes(outcomesSetMockData(AttemptTypesName.ES_CELL.getLabel(), "Wtsi"));

        lenient().when(workUnitService.getAllWorkUnits())
            .thenReturn(List.of(workUnitMockData("Wtsi")));

        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validateData(mutation));

        String expectedMessage =
            "Mutation:(Rsph3b<tma(IMPC)Wtsi>) Mutation symbol(s) are not in the correct format";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateDataMutationGeneConsortiumEmpty() {
        Mutation mutation = new Mutation();
        Set<Gene> geneList = new HashSet<>();
        geneList.add(geneMockData("Rsph3b"));
        mutation.setGenes(geneList);
        mutation.setMolecularMutationType(molecularMutationTypeMockData());
        mutation.setSymbol("Rsph3b<tm1a()Wtsi>");
        mutation.setOutcomes(outcomesSetMockData(AttemptTypesName.ES_CELL.getLabel(), "Wtsi"));

        lenient().when(workUnitService.getAllWorkUnits())
            .thenReturn(List.of(workUnitMockData("Wtsi")));

        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validateData(mutation));

        String expectedMessage =
            "Mutation:(Rsph3b<tm1a()Wtsi>) Mutation symbol(s) are not in the correct format";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateDataMutationGeneCategorizationEmpty() {
        Mutation mutation = new Mutation();
        Set<Gene> geneList = new HashSet<>();
        geneList.add(geneMockData("Rsph3b"));
        mutation.setGenes(geneList);
        mutation.setMolecularMutationType(molecularMutationTypeMockData());
        mutation.setSymbol("Rsph3b<tm1(IMPC)Wtsi>");
        mutation.setOutcomes(outcomesSetMockData(AttemptTypesName.ES_CELL.getLabel(), "Wtsi"));
        mutation.setMutationCategorizations(MutationCategorizationSetMockData("es Cell"));
        lenient().when(workUnitService.getAllWorkUnits())
            .thenReturn(List.of(workUnitMockData("Wtsi")));

        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validateData(mutation));

        String expectedMessage =
            "Mutation:(Rsph3b<tm1(IMPC)Wtsi>) Mutation symbol(s) are not in the correct format";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void validateDataMutationSymbolNotUnique() {
        Mutation mutation = new Mutation();
        Set<Gene> geneList = new HashSet<>();
        geneList.add(geneMockData("Rsph3b"));
        mutation.setGenes(geneList);
        mutation.setMolecularMutationType(molecularMutationTypeMockData());
        mutation.setSymbol("Rsph3b<tm1a(IMPC)Wtsi>");
        mutation.setOutcomes(outcomesSetMockData(AttemptTypesName.ES_CELL.getLabel(), "Wtsi"));
        mutation.setMutationCategorizations(MutationCategorizationSetMockData("a"));
        lenient().when(workUnitService.getAllWorkUnits())
            .thenReturn(List.of(workUnitMockData("Wtsi")));
        lenient().when(mutationRepository.findAllBySymbolLike(Mockito.anyString()))
            .thenReturn(List.of(mutation, mutation));

        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validateData(mutation));

        String expectedMessage =
            "Mutation:(Rsph3b<tm1a(IMPC)Wtsi>) Entered mutation symbol(s) are not unique";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void validateDataMutationEsCellAttemptTypeWrong() {
        Mutation mutation = new Mutation();
        Set<Gene> geneList = new HashSet<>();
        geneList.add(geneMockData("Rsph3b"));
        mutation.setGenes(geneList);
        mutation.setMolecularMutationType(molecularMutationTypeMockData());
        mutation.setSymbol("Rsph3b<tm1b(IMPC)Wtsi>");
        mutation.setOutcomes(outcomesSetMockData(AttemptTypesName.ES_CELL.getLabel(), "Wtsi"));
        mutation.setMutationCategorizations(MutationCategorizationSetMockData("b"));
        lenient().when(workUnitService.getAllWorkUnits())
            .thenReturn(List.of(workUnitMockData("Wtsi")));

        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validateData(mutation));

        String expectedMessage =
            "Error: Please Select one of these mutation types for the initial es cell production attempt (a, e, '')";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateDataEsCellModificationAttemptTypeWrong() {
        Mutation mutation = new Mutation();
        Set<Gene> geneList = new HashSet<>();
        geneList.add(geneMockData("Rsph3b"));
        mutation.setGenes(geneList);
        mutation.setMolecularMutationType(molecularMutationTypeMockData());
        mutation.setSymbol("Rsph3b<tm1a(IMPC)Wtsi>");
        mutation.setOutcomes(
            outcomesSetMockData(AttemptTypesName.ES_CELL_ALLELE_MODIFICATION.getLabel(), "Wtsi"));
        mutation.setMutationCategorizations(MutationCategorizationSetMockData("a"));
        lenient().when(workUnitService.getAllWorkUnits())
            .thenReturn(List.of(workUnitMockData("Wtsi")));
        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validateData(mutation));

        String expectedMessage =
            "Please Select one of these mutation types for es cell modification attempt (b, c, d, e.1, .1, .2)";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateDataWrongFastaFormat() {
        Mutation mutation = new Mutation();
        Set<Gene> geneList = new HashSet<>();
        geneList.add(geneMockData("Rsph3b"));
        mutation.setGenes(geneList);
        mutation.setMolecularMutationType(molecularMutationTypeMockData());
        mutation.setSymbol("Rsph3b<tm1a(IMPC)Wtsi>");
        mutation.setOutcomes(outcomesSetMockData(AttemptTypesName.ES_CELL.getLabel(), "Wtsi"));
        mutation.setMutationCategorizations(MutationCategorizationSetMockData("a"));
        mutation.setMutationSequences(mutationSequencesSetMockData("AATTTCCCGGGGD"));
        lenient().when(workUnitService.getAllWorkUnits())
            .thenReturn(List.of(workUnitMockData("Wtsi")));

        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validateData(mutation));

        String expectedMessage =
            "Error: Sequence Is Not In FASTA Format";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void validateData() {
        Mutation mutation = new Mutation();
        Set<Gene> geneList = new HashSet<>();
        geneList.add(geneMockData("Rsph3b"));
        mutation.setGenes(geneList);
        mutation.setMolecularMutationType(molecularMutationTypeMockData());
        mutation.setSymbol("Rsph3b<tm1a(IMPC)Wtsi>");
        mutation.setOutcomes(outcomesSetMockData(AttemptTypesName.ES_CELL.getLabel(), "Wtsi"));
        mutation.setMutationCategorizations(MutationCategorizationSetMockData("a"));
        mutation
            .setMutationSequences(mutationSequencesSetMockData(">test sequence\nAATTTCCCGGGGD"));
        lenient().when(workUnitService.getAllWorkUnits())
            .thenReturn(List.of(workUnitMockData("Wtsi")));

        assertDoesNotThrow(() ->
            testInstance.validateData(mutation)
        );

    }

    @Test
    void validateReadPermissions() {
    }


    private Set<Gene> geneSetMockData(String geneName) {
        Set<Gene> geneList = new HashSet<>();
        geneList.add(geneMockData(geneName));
        return geneList;
    }

    private Gene geneMockData(String geneName) {
        Gene gene = new Gene();
        gene.setId(1L);
        gene.setSymbol(geneName);
        gene.setAccId("MGI_00000001");
        return gene;
    }


    private Set<Outcome> outcomesSetMockData(String attemptTypeName, String ilarCode) {
        Set<Outcome> outcomes = new HashSet<>();
        outcomes.add(outcomeMockData(attemptTypeName, ilarCode));
        return outcomes;
    }

    private Outcome outcomeMockData(String attemptTypeName, String ilarCode) {
        Outcome outcome = new Outcome();
        outcome.setId(1L);
        outcome.setPlan(planMockData(attemptTypeName, ilarCode));
        return outcome;
    }

    private Plan planMockData(String attemptTypeName, String ilarCode) {
        Plan plan = new Plan();
        plan.setId(1L);
        plan.setWorkUnit(workUnitMockData(ilarCode));
        plan.setWorkGroup(workGroupMockData());
        plan.setAttemptType(attemptTypeMockData(attemptTypeName));
        return plan;
    }

    private WorkUnit workUnitMockData(String ilarCode) {
        WorkUnit workUnit = new WorkUnit();
        workUnit.setId(1L);
        workUnit.setName("TEST_WORK_UNIT_NAME");
        workUnit.setIlarCode(ilarCode);
        return workUnit;
    }

    private WorkGroup workGroupMockData() {
        WorkGroup workGroup = new WorkGroup();
        workGroup.setId(1L);
        workGroup.setName("TEST_WORK_GROUP_NAME");
        return workGroup;
    }

    private AttemptType attemptTypeMockData(String attemptTypeName) {
        AttemptType attemptType = new AttemptType();
        attemptType.setId(1L);
        attemptType.setName(attemptTypeName);
        return attemptType;
    }

    private Set<MutationCategorization> MutationCategorizationSetMockData(String name) {
        Set<MutationCategorization> mutationCategorizationSet = new HashSet<>();
        mutationCategorizationSet.add(MutationCategorizationMockData(name));
        return mutationCategorizationSet;
    }

    private MutationCategorization MutationCategorizationMockData(String name) {
        MutationCategorization mutationCategorization = new MutationCategorization();
        mutationCategorization.setId(1L);
        mutationCategorization.setMutationCategorizationType(MutationCategorizationTypeMockData());
        mutationCategorization.setName(name);
        return mutationCategorization;
    }

    private MutationCategorizationType MutationCategorizationTypeMockData() {
        MutationCategorizationType mutationCategorizationType = new MutationCategorizationType();
        mutationCategorizationType.setId(1L);
        mutationCategorizationType.setName("esc_allele_class");
        return mutationCategorizationType;

    }

    private Set<MutationSequence> mutationSequencesSetMockData(String sequenceString) {
        Set<MutationSequence> mutationSequencesSet = new HashSet<>();
        mutationSequencesSet.add(mutationSequences(sequenceString));
        return mutationSequencesSet;
    }

    private MutationSequence mutationSequences(String sequenceString) {
        MutationSequence mutationSequence = new MutationSequence();
        mutationSequence.setId(1L);
        mutationSequence.setSequence(sequenceMockData(sequenceString));
        return mutationSequence;
    }

    private Sequence sequenceMockData(String sequenceString) {
        Sequence sequence = new Sequence();
        sequence.setId(1L);
        sequence.setSequence(sequenceString);
        return sequence;
    }

}