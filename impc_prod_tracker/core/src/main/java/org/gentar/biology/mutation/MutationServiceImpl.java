package org.gentar.biology.mutation;

import org.gentar.audit.history.History;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.gentar.biology.mutation.sequence.MutationSequenceService;
import org.gentar.biology.sequence.SequenceService;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MutationServiceImpl implements MutationService
{
    private MutationRepository mutationRepository;
    private SequenceService sequenceService;
    private MutationSequenceService mutationSequenceService;
    private MutationUpdater mutationUpdater;

    private static final String MUTATION_NOT_EXIST_ERROR = "Mutation %s does not exist.";
    private static final String MUTATION_BY_ID_NOT_EXIST_ERROR = "Mutation with id [%s] does not exist.";

    public MutationServiceImpl(
        MutationRepository mutationRepository,
        SequenceService sequenceService,
        MutationSequenceService mutationSequenceService,
        MutationUpdater mutationUpdater)
    {
        this.mutationRepository = mutationRepository;
        this.sequenceService = sequenceService;
        this.mutationSequenceService = mutationSequenceService;
        this.mutationUpdater = mutationUpdater;
    }

    @Override
    public Mutation getById(Long id)
    {
        return mutationRepository.findFirstById(id);
    }

    @Override
    public Mutation getByIdFailsIfNull(Long id)
    {
        Mutation mutation = getById(id);
        if (mutation == null)
        {
            throw new UserOperationFailedException(String.format(MUTATION_BY_ID_NOT_EXIST_ERROR, id));
        }
        return mutation;
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
