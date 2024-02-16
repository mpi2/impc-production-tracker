package org.gentar.biology.mutation.formatter;

import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.mutation.MutationRepository;
import org.gentar.biology.mutation.MutationServiceImpl;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.sequence.Sequence;
import org.gentar.organization.work_unit.WorkUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.gentar.mockdata.MockData.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.lenient;

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