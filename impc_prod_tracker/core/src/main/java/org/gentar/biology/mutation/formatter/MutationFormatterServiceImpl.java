package org.gentar.biology.mutation.formatter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.gentar.biology.colony.Colony;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.mutation.MutationRepository;
import org.gentar.biology.mutation.MutationServiceImpl;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.sequence.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MutationFormatterServiceImpl implements MutationFormatterService {
    private final MutationRepository mutationRepository;
    private final MutationServiceImpl mutationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(MutationFormatterServiceImpl.class);;
    public MutationFormatterServiceImpl(
        MutationRepository mutationRepository,
        MutationServiceImpl mutationService) {

        this.mutationRepository = mutationRepository;
        this.mutationService = mutationService;
    }

    @Override
    public void formatSequence(String workUnit) {
        try {
            List<Mutation> allMutations = (List<Mutation>) mutationRepository.findAll();
            List<Mutation> unValidatedMutations =
                getUnValidatedMutations(workUnit, allMutations);
            LOGGER.error("unValidatedMutations size:" + unValidatedMutations.size());
            unValidatedMutations.forEach(mutation -> {
                Mutation validatedMutation = getValidatedMutation(mutation);
                LOGGER.error(" Mutation validatedMutation");
                mutationService.update(validatedMutation);
                LOGGER.error("unValidatedMutations formatted");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Mutation getValidatedMutation(Mutation mutation) {
        LOGGER.error("getValidatedMutation");
        Mutation validatedMutation = new Mutation(mutation);
        validatedMutation.getMutationSequences().forEach(ms ->
            ms.setSequence(sequenceFormatter(getColonyNames(mutation).get(0),
                getSequences(mutation).get(0)))
        );
        LOGGER.error("getValidatedMutation end");
        return validatedMutation;
    }

    private List<Mutation> getUnValidatedMutations(String workUnit, List<Mutation> allMutations) {
        return getMutationsByWorkUnit(allMutations, workUnit)
            .stream()
            .filter(s -> !isSequenceInFastaFormat(s))
            .collect(Collectors.toList());
    }

    private List<String> getColonyNames(Mutation m) {
        return m.getOutcomes()
            .stream()
            .map(Outcome::getColony)
            .map(Colony::getName)
            .collect(Collectors.toList());
    }

    private List<Sequence> getSequences(Mutation mutation) {
        return mutation.getMutationSequences()
            .stream()
            .map(MutationSequence::getSequence)
            .collect(Collectors.toList());
    }

    private List<Mutation> getMutationsByWorkUnit(List<Mutation> mutations, String workUnit) {
        List<Mutation> mutationsByWorkUnit = new ArrayList<>();
        for (Mutation mutation : mutations) {
            fillMutationsByWorkUnits(workUnit, mutationsByWorkUnit, mutation);
        }
        return mutationsByWorkUnit;
    }

    private void fillMutationsByWorkUnits(String workUnit, List<Mutation> mutationsByWorkUnit,
                                          Mutation mutation) {
        for (Outcome outcome : mutation.getOutcomes()) {
            if (outcome.getPlan().getWorkUnit().getName().equals(workUnit)) {
                mutationsByWorkUnit.add(mutation);
            }
        }
    }

    private boolean isSequenceInFastaFormat(Mutation mutation) {
        List<Sequence> sequences = getSequences(mutation);
        if (sequences.stream().anyMatch(s -> s.getSequence() == null)) {
            return false;
        }

        List<String> sequenceString = sequences
            .stream()
            .map(Sequence::getSequence)
            .collect(Collectors.toList());

        return sequenceString
            .stream()
            .noneMatch(s ->
                !isStartWithBiggerSymbol(s) || !isSequenceHeaderSingleLine(s)
                    || !isDnaFormatCorrect(s));
    }

    private Boolean isStartWithBiggerSymbol(String sequence) {
        return sequence.charAt(0) == '>';
    }

    private Boolean isSequenceHeaderSingleLine(String sequence) {
        return sequence.split("\n").length == 2
            && (sequence.split("\n")[0].length() > 0
            && sequence.split("\n")[0].length() < 256);

    }

    private Boolean isDnaFormatCorrect(String sequence) {

        String dna = sequence.split("\n")[1];
        for (int i = 0; i < dna.length(); i++) {
            if (!dnaCharacters().contains(dna.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private List<Character> dnaCharacters() {
        return List
            .of('A', 'C', 'G', 'T', 'U', 'I', 'R', 'Y', 'K', 'M', 'S', 'W', 'B', 'D', 'H', 'V',
                'N', 'a', 'c', 'g', 't', 'u', 'i', 'r', 'y', 'k', 'm', 's', 'w', 'b', 'd', 'h', 'v',
                'n');
    }

    private Sequence sequenceFormatter(String colonyName, Sequence sequence) {
        LOGGER.error("sequenceFormatter");
        Sequence formattedSequence = new Sequence(sequence);
        formattedSequence
            .setSequence(">" + colonyName + System.lineSeparator() + sequence.getSequence());
        return formattedSequence;
    }
}
