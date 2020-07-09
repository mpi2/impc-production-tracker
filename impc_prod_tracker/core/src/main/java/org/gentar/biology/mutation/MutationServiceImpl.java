package org.gentar.biology.mutation;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.gentar.biology.mutation.sequence.MutationSequenceService;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.sequence.SequenceService;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class MutationServiceImpl implements MutationService
{
    private final MutationRepository mutationRepository;
    private final SequenceService sequenceService;
    private final MutationSequenceService mutationSequenceService;
    private final MutationUpdater mutationUpdater;
    private final HistoryService<Mutation> historyService;

    private static final String MUTATION_NOT_EXIST_ERROR = "Mutation %s does not exist.";

    public MutationServiceImpl(
        MutationRepository mutationRepository,
        SequenceService sequenceService,
        MutationSequenceService mutationSequenceService,
        MutationUpdater mutationUpdater,
        HistoryService<Mutation> historyService)
    {
        this.mutationRepository = mutationRepository;
        this.sequenceService = sequenceService;
        this.mutationSequenceService = mutationSequenceService;
        this.mutationUpdater = mutationUpdater;
        this.historyService = historyService;
    }

    @Override
    public Mutation getById(Long id)
    {
        return mutationRepository.findFirstById(id);
    }

    @Override
    public Mutation getMutationByMinFailsIfNull(String min)
    {
        Mutation mutation = mutationRepository.findByMin(min);
        if (mutation == null)
        {
            throw new UserOperationFailedException(String.format(MUTATION_NOT_EXIST_ERROR, min));
        }
        return mutation;
    }

    @Override
    public Mutation create(Mutation mutation)
    {
        Mutation createdMutation = mutationRepository.save(mutation);
        createdMutation.setMin(buildMin(createdMutation.getId()));
        saveMutationSequences(mutation);
        return createdMutation;
    }

    @Override
    public History update(Mutation mutation)
    {
        Mutation originalMutation = new Mutation(getMutationByMinFailsIfNull(mutation.getMin()));
        return mutationUpdater.update(originalMutation, mutation);
    }

    @Override
    public List<History> getHistory(Mutation mutation)
    {
        return historyService.getHistoryByEntityNameAndEntityId(
            Mutation.class.getSimpleName(), mutation.getId());
    }

    private String buildMin(Long id)
    {
        String identifier = String.format("%0" + 12 + "d", id);
        identifier = "MIN:" + identifier;
        return identifier;
    }

    private void saveMutationSequences(Mutation mutation)
    {
        Set<MutationSequence> mutationSequences = mutation.getMutationSequences();
        if (mutationSequences != null)
        {
            mutationSequences.forEach( x -> {
                sequenceService.createSequence(x.getSequence());
                mutationSequenceService.createSequence(x);
            });
        }
    }
}
