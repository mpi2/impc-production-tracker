package org.gentar.audit.diff;

import org.gentar.biology.location.Location;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.gentar.biology.sequence.Sequence;
import org.gentar.biology.sequence_location.SequenceLocation;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

// This class contains some edges cases for the detection of changes. The Mutation class is chosen
// because that's one with complex cases of collections with collections inside.
class ChangesDetectorComplexCasesTest
{
    @Test
    void testEmptyObjects()
    {
        ChangesDetector<Mutation> testInstance =
            new ChangesDetector<>(Collections.emptyList(), new Mutation(), new Mutation());
        List<ChangeEntry> changeEntries = testInstance.getChanges();

        assertThat("No changes expected:", changeEntries.isEmpty(), is(true));
    }

    @Test
    void testEqualObjects()
    {
        Mutation originalMutation = buildMutation();
        Mutation newMutation = new Mutation(originalMutation);
        ChangesDetector<Mutation> testInstance =
            new ChangesDetector<>(Collections.emptyList(), originalMutation, newMutation);
        List<ChangeEntry> changeEntries = testInstance.getChanges();

        assertThat("No changes expected:", changeEntries.isEmpty(), is(true));
    }

    @Test
    void testChangeInElementCollectionInsideCollection()
    {
        Mutation originalMutation = buildMutation();
        Mutation newMutation = new Mutation(originalMutation);
        newMutation = editMutation(newMutation);

        ChangesDetector<Mutation> testInstance =
            new ChangesDetector<>(Collections.emptyList(), originalMutation, newMutation);
        List<ChangeEntry> changeEntries = testInstance.getChanges();

        assertThat("Changes expected:", changeEntries.isEmpty(), is(false));
    }

    private ChangeEntry findByName(String name, List<ChangeEntry> changeEntries)
    {
        return changeEntries.stream()
            .filter(x -> x.getProperty().equals(name))
            .findAny().orElse(null);
    }

    @Test
    void testChangeInElementCollectionInsideCollectionWithSequence()
    {
        Sequence originalSequence = buildSequence();
        Sequence newSequence = buildSequence();
        newSequence = editSequence(newSequence);
        ChangesDetector<Sequence> testInstance =
            new ChangesDetector<>(Collections.emptyList(), originalSequence, newSequence);
        List<ChangeEntry> changeEntries = testInstance.getChanges();

        assertThat("2 changes expected:", changeEntries.size(), is(2));

        ChangeEntry changeEntry1 = findByName("sequenceLocations.[1]", changeEntries);
        assertThat(changeEntry1.getChangeType(), is(ChangeType.CHANGED_ELEMENT));
        assertThat(changeEntry1.getType(), is(SequenceLocation.class));

        ChangeEntry changeEntry2 = findByName("sequenceLocations.[1].location.chr", changeEntries);
        assertThat(changeEntry2.getChangeType(), is(ChangeType.CHANGED_FIELD));
        assertThat(changeEntry2.getType(), is(String.class));
        assertThat(changeEntry2.getOldValue(), is("X"));
        assertThat(changeEntry2.getNewValue(), is("Y"));
    }

    private Sequence editSequence(Sequence newSequence)
    {
        for (SequenceLocation sequenceLocation : newSequence.getSequenceLocations())
        {
            Location location = sequenceLocation.getLocation();
            location.setChr("Y");
        }
        return newSequence;
    }

    private Mutation editMutation(Mutation mutation)
    {
        for (MutationSequence mutationSequence : mutation.getMutationSequences())
        {
            Sequence sequence = mutationSequence.getSequence();
            for (SequenceLocation sequenceLocation : sequence.getSequenceLocations())
            {
                Location location = sequenceLocation.getLocation();
                location.setChr("Y");
            }
        }
        return mutation;
    }

    private Mutation buildMutation()
    {
        Mutation mutation = new Mutation();
        mutation.setId(1L);
        Set<MutationSequence> mutationSequences = new HashSet<>();
        mutationSequences.add(buildMutationSequence());
        mutation.setMutationSequences(mutationSequences);
        return mutation;
    }

    private MutationSequence buildMutationSequence()
    {
        MutationSequence mutationSequence = new MutationSequence();
        mutationSequence.setId(1L);
        mutationSequence.setIndex(1);
        mutationSequence.setSequence(buildSequence());
        return mutationSequence;
    }

    private Sequence buildSequence()
    {
        Sequence sequence = new Sequence();
        sequence.setId(1L);
        sequence.setSequence("sequenceText");
        List<SequenceLocation> sequenceLocations = new ArrayList<>();
        sequenceLocations.add(buildSequenceLocation());
        sequence.setSequenceLocations(sequenceLocations);
        return sequence;
    }

    private SequenceLocation buildSequenceLocation()
    {
        SequenceLocation sequenceLocation = new SequenceLocation();
        sequenceLocation.setId(1L);
        sequenceLocation.setIndex(1);
        sequenceLocation.setLocation(buildLocation());
        return sequenceLocation;
    }

    private Location buildLocation()
    {
        Location location = new Location();
        location.setId(1L);
        location.setChr("X");
        return location;
    }
}