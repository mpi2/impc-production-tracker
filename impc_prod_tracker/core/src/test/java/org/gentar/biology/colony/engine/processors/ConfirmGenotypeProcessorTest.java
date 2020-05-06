package org.gentar.biology.colony.engine.processors;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.engine.ColonyEvent;
import org.gentar.biology.colony.engine.ColonyState;
import org.gentar.biology.colony.engine.ColonyStateSetter;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.sequence.Sequence;
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

    private SequenceService sequenceService = new SequenceService();

    @BeforeEach
    void setUp()
    {
        testInstance = new ConfirmGenotypeProcessor(colonyStateSetter, sequenceService);
    }

    @Test
    public void testWhenNoInformationExists()
    {
        Colony colony = buildColony(1L, ColonyState.GenotypeInProgress.getInternalName());
        colony.setEvent(ColonyEvent.confirmGenotypeWhenInProgress);

        UserOperationFailedException thrown = assertThrows(UserOperationFailedException.class,
            () -> testInstance.process(colony), "Exception not thrown");
        assertTransitionCannotBeExecuted(thrown);
    }

    @Test
    public void testWhenOutcomeSequenceExists()
    {
        Colony colony = buildColonyWithOutcomeSequence(1L);
        colony.setEvent(ColonyEvent.confirmGenotypeWhenInProgress);

        UserOperationFailedException thrown = assertThrows(UserOperationFailedException.class,
            () -> testInstance.process(colony), "Exception not thrown");
        assertTransitionCannotBeExecuted(thrown);
    }

    @Test
    public void testWhenSequenceExistsButItIsNotOutcomeCategory()
    {
        Colony colony = buildColony(1L, ColonyState.GenotypeInProgress.getInternalName());
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
        colony.setEvent(ColonyEvent.confirmGenotypeWhenInProgress);

        UserOperationFailedException thrown = assertThrows(UserOperationFailedException.class,
            () -> testInstance.process(colony), "Exception not thrown");
        assertTransitionCannotBeExecuted(thrown);
    }

    @Test
    public void testWhenSequenceExistsAndVcfFileAndVcfIndexExist()
    {
        Colony colony = buildColonyWithOutcomeSequence(1L);
        Mutation mutation = colony.getOutcome().getMutations().iterator().next();
        byte[] vcfFile = "vcf file content".getBytes();
        byte[] vcfIndex = "vcf index content".getBytes();
        mutation.setVcfFile(vcfFile);
        mutation.setVcfFileIndex(vcfIndex);
        colony.setEvent(ColonyEvent.confirmGenotypeWhenInProgress);

        testInstance.process(colony);

        verify(colonyStateSetter, times(1)).setStatusByName(any(Colony.class), any(String.class));
    }

    @Test
    public void testWhenSequenceExistsAndVcfFileExistsAndVcfIndexDoesNotExist()
    {
        Colony colony = buildColonyWithOutcomeSequence(1L);
        Mutation mutation = colony.getOutcome().getMutations().iterator().next();
        byte[] byteContent = "vcf content".getBytes();
        mutation.setVcfFile(byteContent);
        colony.setEvent(ColonyEvent.confirmGenotypeWhenInProgress);

        UserOperationFailedException thrown = assertThrows(UserOperationFailedException.class,
            () -> testInstance.process(colony), "Exception not thrown");
        assertTransitionCannotBeExecuted(thrown);
    }

    @Test
    public void testWhenSequenceExistsAndVcfFileDoesNotExistsAndVcfIndexExist()
    {
        Colony colony = buildColonyWithOutcomeSequence(1L);
        Mutation mutation = colony.getOutcome().getMutations().iterator().next();
        byte[] byteContent = "vcf index content".getBytes();
        mutation.setVcfFileIndex(byteContent);
        colony.setEvent(ColonyEvent.confirmGenotypeWhenInProgress);

        UserOperationFailedException thrown = assertThrows(UserOperationFailedException.class,
            () -> testInstance.process(colony), "Exception not thrown");
        assertTransitionCannotBeExecuted(thrown);
    }

    @Test
    public void testWhenSequenceExistsAndVcfFileExistsAndVcfIndexExist()
    {
        Colony colony = buildColonyWithOutcomeSequence(1L);
        Mutation mutation = colony.getOutcome().getMutations().iterator().next();
        byte[] vcfFile = "vcf file content".getBytes();
        byte[] vcfIndex = "vcf index content".getBytes();
        mutation.setVcfFile(vcfFile);
        mutation.setVcfFileIndex(vcfIndex);
        colony.setEvent(ColonyEvent.confirmGenotypeWhenInProgress);

        testInstance.process(colony);

        verify(colonyStateSetter, times(1)).setStatusByName(any(Colony.class), any(String.class));
    }

    @Test
    public void testWhenSequenceExistsAndBamFileExistsAndBamIndexDoesNotExist()
    {
        Colony colony = buildColonyWithOutcomeSequence(1L);
        Mutation mutation = colony.getOutcome().getMutations().iterator().next();
        byte[] byteContent = "bam content".getBytes();
        mutation.setBamFile(byteContent);
        colony.setEvent(ColonyEvent.confirmGenotypeWhenInProgress);

        UserOperationFailedException thrown = assertThrows(UserOperationFailedException.class,
            () -> testInstance.process(colony), "Exception not thrown");
        assertTransitionCannotBeExecuted(thrown);
    }

    @Test
    public void testSequenceExistsAndWhenBamFileDoesNotExistsAndBamIndexExist()
    {
        Colony colony = buildColonyWithOutcomeSequence(1L);
        Mutation mutation = colony.getOutcome().getMutations().iterator().next();
        byte[] byteContent = "bam index content".getBytes();
        mutation.setBamFileIndex(byteContent);
        colony.setEvent(ColonyEvent.confirmGenotypeWhenInProgress);

        UserOperationFailedException thrown = assertThrows(UserOperationFailedException.class,
            () -> testInstance.process(colony), "Exception not thrown");
        assertTransitionCannotBeExecuted(thrown);
    }

    @Test
    public void testWhenSequenceExistsAndBamFileAndVBamIndexExist()
    {
        Colony colony = buildColonyWithOutcomeSequence(1L);
        Mutation mutation = colony.getOutcome().getMutations().iterator().next();
        byte[] bamFile = "bam file content".getBytes();
        byte[] bamIndex = "bam index content".getBytes();
        mutation.setBamFile(bamFile);
        mutation.setBamFileIndex(bamIndex);
        colony.setEvent(ColonyEvent.confirmGenotypeWhenInProgress);

        testInstance.process(colony);

        verify(colonyStateSetter, times(1)).setStatusByName(any(Colony.class), any(String.class));
    }

    private void assertTransitionCannotBeExecuted(UserOperationFailedException thrown)
    {
        assertThat(
            "Not expected message", thrown.getMessage(), is("Transition cannot be executed"));
        assertThat(
            "Not expected message",
            thrown.getDebugMessage(),
            is("Please check that a sequence is associated or that there is information" +
                " for the vcf or bam file."));
        verify(colonyStateSetter, times(0)).setStatusByName(any(Colony.class), any(String.class));
    }

    private Colony buildColony(Long id, String statusName)
    {
        Outcome outcome = OutcomeBuilder.getInstance().withId(id).withColony().build();
        Status status = new Status();
        status.setName(statusName);
        Colony colony = outcome.getColony();
        colony.setStatus(status);
        return colony;
    }

    private Colony buildColonyWithOutcomeSequence(Long id)
    {
        Colony colony = buildColony(id, ColonyState.GenotypeInProgress.getInternalName());
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
