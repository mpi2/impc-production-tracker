package org.gentar.biology.colony.engine.processors;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.engine.ColonyEvent;
import org.gentar.biology.colony.engine.ColonyState;
import org.gentar.biology.colony.engine.ColonyStateSetter;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.sequence.Sequence;
import org.gentar.biology.sequence.SequenceRepository;
import org.gentar.biology.sequence.SequenceService;
import org.gentar.biology.sequence.category.SequenceCategory;
import org.gentar.biology.sequence.category.SequenceCategoryName;
import org.gentar.biology.status.Status;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.test_util.OutcomeBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConfirmGenotypeProcessorTest
{
    private ConfirmGenotypeProcessor testInstance;

    @Mock
    private ColonyStateSetter colonyStateSetter;

    @Mock
    private SequenceRepository sequenceRepository;

    private final SequenceService sequenceService = new SequenceService(sequenceRepository);

    @BeforeEach
    void setUp()
    {
        testInstance = new ConfirmGenotypeProcessor(colonyStateSetter, sequenceService);
    }

    @Test
    public void testWhenNoInformationExists()
    {
        Colony colony = buildColony(1L, ColonyState.GenotypeInProgress.getInternalName());
        colony.setProcessDataEvent(ColonyEvent.confirmGenotypeWhenInProgress);

        UserOperationFailedException thrown = assertThrows(UserOperationFailedException.class,
            () -> testInstance.process(colony), "Exception not thrown");
        assertTransitionCannotBeExecuted(thrown);
    }

    @Test
    public void testWhenOutcomeSequenceExists()
    {
        Colony colony = buildColonyWithOutcomeSequence(1L);
        colony.setProcessDataEvent(ColonyEvent.confirmGenotypeWhenInProgress);

        UserOperationFailedException thrown = assertThrows(UserOperationFailedException.class,
            () -> testInstance.process(colony), "Transition cannot be executed At least one sequence and All mutation symbols must exist.");
        assertTransitionCannotBeExecuted(thrown);
    }

    @Test
    public void testWhenSequenceExistsButItIsNotOutcomeCategory()
    {
        Colony colony = buildColony(1L, ColonyState.GenotypeInProgress.getInternalName());
        colony.setLegacyModification(false);
        colony.setLegacyWithoutSequence(false);
        Mutation mutation = new Mutation();
        MutationSequence mutationSequence = new MutationSequence();
        Sequence sequence = new Sequence();
        SequenceCategory sequenceCategory = new SequenceCategory();
        sequenceCategory.setName(SequenceCategoryName.INTENTION_TARGET_SEQUENCE.getLabel());
        sequence.setSequenceCategory(sequenceCategory);
        mutationSequence.setSequence(sequence);
        mutationSequence.setMutation(mutation);
        Set<MutationSequence> mutationSequences = new HashSet<>();
        mutationSequences.add(mutationSequence);
        mutation.setMutationSequences(mutationSequences);
        Set<Mutation> mutations = new HashSet<>();
        mutations.add(mutation);
        colony.getOutcome().setMutations(mutations);
        colony.setProcessDataEvent(ColonyEvent.confirmGenotypeWhenInProgress);

        UserOperationFailedException thrown = assertThrows(UserOperationFailedException.class,
            () -> testInstance.process(colony), "Exception not thrown");
        assertTransitionCannotBeExecuted(thrown);
    }

    @Test
    public void testWhenSequenceExistsAndMgiAlleleSymbolExists()
    {
        Colony colony = buildColonyWithOutcomeSequence(1L);
        Mutation mutation = colony.getOutcome().getMutations().iterator().next();
        mutation.setSymbol("mgiAlleleSymbol");
        colony.setProcessDataEvent(ColonyEvent.confirmGenotypeWhenInProgress);

        testInstance.process(colony);

        verify(colonyStateSetter, times(1)).setStatusByName(any(Colony.class),
                any(String.class));
    }
//
//    @Test
//    public void testWhenSequenceDoesNotExistsAndLegacyWithoutSequenceIsTrueAndMgiAlleleSymbolExists()
//    {
//        Colony colony = buildColony(1L, ColonyState.GenotypeInProgress.getInternalName());
//        Mutation mutation = new Mutation();
//        Set<Mutation> mutations = new HashSet<>();
//        mutations.add(mutation);
//        colony.getOutcome().setMutations(mutations);
//        mutation.setSymbol("mgiAlleleSymbol");
//        colony.setLegacyModification(false);
//        colony.setLegacyWithoutSequence(true);
//        colony.setEvent(ColonyEvent.confirmGenotypeWhenInProgress);
//
//        testInstance.process(colony);
//
//        verify(colonyStateSetter, times(1)).setStatusByName(any(Colony.class),
//                any(String.class));
//    }

    @Test
    public void testWhenSequenceDoesNotExistsAndMgiAlleleSymbolExists()
    {
        Colony colony = buildColony(1L, ColonyState.GenotypeInProgress.getInternalName());
        Mutation mutation = new Mutation();
        Set<Mutation> mutations = new HashSet<>();
        mutations.add(mutation);
        colony.getOutcome().setMutations(mutations);
        mutation.setSymbol("mgiAlleleSymbol");
        colony.setLegacyModification(false);
        colony.setLegacyWithoutSequence(false);
        colony.setProcessDataEvent(ColonyEvent.confirmGenotypeWhenInProgress);

        UserOperationFailedException thrown = assertThrows(UserOperationFailedException.class,
            () -> testInstance.process(colony), "Exception not thrown");
        assertTransitionCannotBeExecuted(thrown);
    }

    private void assertTransitionCannotBeExecuted(UserOperationFailedException thrown)
    {
        assertThat(
            "Not expected message", thrown.getMessage(), is("Transition cannot be executed At least one sequence and All mutation symbols must exist."));

        verify(colonyStateSetter, times(0)).setStatusByName(any(Colony.class), any(String.class));
    }

    private Colony buildColony(Long id, String statusName)
    {
        Outcome outcome = OutcomeBuilder.getInstance().withId(id).withColony().build();
        Status status = new Status();
        status.setName(statusName);
        Colony colony = outcome.getColony();
        colony.setProcessDataStatus(status);
        return colony;
    }

    private Colony buildColonyWithOutcomeSequence(Long id)
    {
        Colony colony = buildColony(id, ColonyState.GenotypeInProgress.getInternalName());
        colony.setLegacyModification(false);
        colony.setLegacyWithoutSequence(false);
        Mutation mutation = new Mutation();
        MutationSequence mutationSequence = new MutationSequence();
        Sequence sequence = new Sequence();
        SequenceCategory sequenceCategory = new SequenceCategory();
        sequenceCategory.setName(SequenceCategoryName.OUTCOME_SEQUENCE.getLabel());
        sequence.setSequenceCategory(sequenceCategory);
        mutationSequence.setSequence(sequence);
        mutationSequence.setMutation(mutation);
        Set<MutationSequence> mutationSequences = new HashSet<>();
        mutationSequences.add(mutationSequence);
        mutation.setMutationSequences(mutationSequences);
        Set<Mutation> mutations = new HashSet<>();
        mutations.add(mutation);
        colony.getOutcome().setMutations(mutations);
        return colony;
    }
}
