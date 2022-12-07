package org.gentar.biology.mutation;

import java.util.ArrayList;
import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.mutation.mgi.MgiAlleleDto;
import org.gentar.biology.mutation.mgi.MgiAlleleResponseDto;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.gentar.biology.mutation.sequence.MutationSequenceService;
import org.gentar.biology.sequence.SequenceService;
import org.gentar.exceptions.NotFoundException;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Set;

@Component
public class MutationServiceImpl implements MutationService {
    private final MutationRepository mutationRepository;
    private final SequenceService sequenceService;
    private final MutationSequenceService mutationSequenceService;
    private final MutationUpdater mutationUpdater;
    private final HistoryService<Mutation> historyService;
    private final MutationValidator mutationValidator;

    private static final String MUTATION_NOT_EXIST_ERROR = "Mutation %s does not exist.";

    public MutationServiceImpl(
        MutationRepository mutationRepository,
        SequenceService sequenceService,
        MutationSequenceService mutationSequenceService,
        MutationUpdater mutationUpdater,
        HistoryService<Mutation> historyService,
        MutationValidator mutationValidator) {
        this.mutationRepository = mutationRepository;
        this.sequenceService = sequenceService;
        this.mutationSequenceService = mutationSequenceService;
        this.mutationUpdater = mutationUpdater;
        this.historyService = historyService;
        this.mutationValidator = mutationValidator;
    }

    @Override
    public Mutation getById(Long id) {
        return mutationRepository.findFirstById(id);
    }

    @Override
    public Mutation getMutationByMinFailsIfNull(String min) {
        Mutation mutation = mutationRepository.findByMin(min);
        if (mutation == null) {
            throw new NotFoundException(String.format(MUTATION_NOT_EXIST_ERROR, min));
        }
        mutationValidator.validateReadPermissions(mutation);
        return mutation;
    }

    @Override
    public Mutation create(Mutation mutation) {
        validateData(mutation);
        Mutation createdMutation = mutationRepository.save(mutation);
        createdMutation.setMin(buildMin(createdMutation.getId()));
        saveMutationSequences(mutation);
        registerCreationInHistory(mutation);
        return createdMutation;
    }

    private void validateData(Mutation mutation) {
        mutationValidator.validateData(mutation);
    }

    private void registerCreationInHistory(Mutation mutation) {
        History history = historyService.buildCreationTrack(mutation, mutation.getId());
        historyService.saveTrackOfChanges(history);
    }

    @Override
    public History update(Mutation mutation) {
        Mutation originalMutation = new Mutation(getMutationByMinFailsIfNull(mutation.getMin()));
        return mutationUpdater.update(originalMutation, mutation);
    }

    @Override
    public List<History> getHistory(Mutation mutation) {
        return historyService.getHistoryByEntityNameAndEntityId(
            Mutation.class.getSimpleName(), mutation.getId());
    }

    @Override
    public MgiAlleleResponseDto updateMutationMgiAlleleId(List<MgiAlleleDto> mgiAlleleDtos) {
        MgiAlleleResponseDto mgiAlleleResponse = new MgiAlleleResponseDto();
        List<MgiAlleleDto> updatedMutations = new ArrayList<>();
        List<MgiAlleleDto> noUpdateNecessaryMutations = new ArrayList<>();
        List<MgiAlleleDto> nullMutations = new ArrayList<>();
        List<MgiAlleleDto> duplicateMutations = new ArrayList<>();


        for (MgiAlleleDto mgiAlleleDto : mgiAlleleDtos) {
            List<Mutation> mutations = mutationRepository.findBySymbol(mgiAlleleDto.getSymbol());
            if (mutations.size() == 0) {
                nullMutations.add(mgiAlleleDto);
            } else if (mutations.size() > 1) {
                duplicateMutations.add(mgiAlleleDto);
            } else if (mgiAlleleDto.getAccId().equals(mutations.get(0).getMgiAlleleId()) &&
                mgiAlleleDto.getSymbol().equals(mutations.get(0).getSymbol())) {
                noUpdateNecessaryMutations.add(mgiAlleleDto);
            } else {
                mutations.get(0).setMgiAlleleId(mgiAlleleDto.getAccId());
                update(mutations.get(0));
                updatedMutations.add(mgiAlleleDto);
            }
        }
        mgiAlleleResponse.setDuplicateMutation(duplicateMutations);
        mgiAlleleResponse.setNoMutation(nullMutations);
        mgiAlleleResponse.setNoUpdateNecessary(noUpdateNecessaryMutations);
        mgiAlleleResponse.setUpdated(updatedMutations);
        return mgiAlleleResponse;
    }


    private String buildMin(Long id) {
        String identifier = String.format("%0" + 12 + "d", id);
        identifier = "MIN:" + identifier;
        return identifier;
    }

    private void saveMutationSequences(Mutation mutation) {
        Set<MutationSequence> mutationSequences = mutation.getMutationSequences();
        if (mutationSequences != null) {
            mutationSequences.forEach(x -> {
                sequenceService.createSequence(x.getSequence());
                mutationSequenceService.createSequence(x);
            });
        }
    }
}
