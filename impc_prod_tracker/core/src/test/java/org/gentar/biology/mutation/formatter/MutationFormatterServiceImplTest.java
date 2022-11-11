package org.gentar.biology.mutation.formatter;

import static org.gentar.mockdata.MockData.mutationMockData;
import static org.gentar.mockdata.MockData.mutationSequenceMockData;
import static org.gentar.mockdata.MockData.outcomeMockData;
import static org.gentar.mockdata.MockData.planMockData;
import static org.gentar.mockdata.MockData.sequenceMockData;
import static org.gentar.mockdata.MockData.workUnitMockData;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

import java.util.List;
import java.util.Set;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.mutation.MutationRepository;
import org.gentar.biology.mutation.MutationServiceImpl;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.sequence.Sequence;
import org.gentar.exceptions.NotFoundException;
import org.gentar.exceptions.SystemOperationFailedException;
import org.gentar.organization.work_unit.WorkUnit;
import org.gentar.security.abac.ResourcePrivacy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MutationFormatterServiceImplTest {

    @Mock
    private MutationRepository mutationRepository;
    @Mock
    private MutationServiceImpl mutationService;

    MutationFormatterServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new MutationFormatterServiceImpl(mutationRepository, mutationService);
    }

    @Test
    void formatSequence() {

        WorkUnit workUnit = workUnitMockData();
        workUnit.setName("WTSI");

        Plan plan = planMockData();
        plan.setWorkUnit(workUnit);

        Outcome outcome = outcomeMockData();
        outcome.setPlan(plan);

        Sequence sequence = sequenceMockData();

        sequence.setSequence(">Otog \n TGGGACTAGGTCACAGACAGAAAAAGGGGGATG" +
            "AGAAGGCCTTAGGGGACTAGGTCACAGACAGAAAAAGGGGGAT" +
            "GAGAAGGCCTTAGGGGACTAGGTCACAGACAGAAAAAGGGGGATGAGAAGGCCTTAG" +
            "ATCTAAGGGAGGTAAAGAGAGAATTAACCACAGGAATTAGCACACTGAAGGATCGTGC" +
            "TCGCCAGACCTCAAACCATCTCCTTCTTGCGTCATGAGA");

        MutationSequence mutationSequence = mutationSequenceMockData();
        mutationSequence.setSequence(sequence);

        Mutation mutation = mutationMockData();
        mutation.setOutcomes(Set.of(outcome));
        mutation.setMutationSequences(Set.of(mutationSequence));

        lenient().when(mutationRepository.findAll())
            .thenReturn(List.of(mutation));


        assertDoesNotThrow(() ->
            testInstance.formatSequence("WTSI")
        );
    }


    @Test
    void formatSequenceSequenceNull() {

        WorkUnit workUnit = workUnitMockData();
        workUnit.setName("WTSI");

        Plan plan = planMockData();
        plan.setWorkUnit(workUnit);

        Outcome outcome = outcomeMockData();
        outcome.setPlan(plan);

        Sequence sequence = sequenceMockData();

        sequence.setSequence(null);

        MutationSequence mutationSequence = mutationSequenceMockData();
        mutationSequence.setSequence(sequence);

        Mutation mutation = mutationMockData();
        mutation.setOutcomes(Set.of(outcome));
        mutation.setMutationSequences(Set.of(mutationSequence));

        lenient().when(mutationRepository.findAll())
            .thenReturn(List.of(mutation));


        assertDoesNotThrow(() ->
            testInstance.formatSequence("WTSI")
        );
    }

}