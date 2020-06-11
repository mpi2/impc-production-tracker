package org.gentar.biology.outcome;

import org.gentar.Mapper;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.mutation.MutationCreationDTO;
import org.gentar.biology.mutation.MutationCreationMapper;
import org.gentar.biology.outcome.type.OutcomeType;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class OutcomeCreationMapper implements Mapper<Outcome, OutcomeCreationDTO>
{
    private OutcomeCommonMapper outcomeCommonMapper;
    private OutcomeService outcomeService;
    private MutationCreationMapper mutationCreationMapper;

    public OutcomeCreationMapper(
        OutcomeCommonMapper outcomeCommonMapper,
        OutcomeService outcomeService,
        MutationCreationMapper mutationCreationMapper)
    {
        this.outcomeCommonMapper = outcomeCommonMapper;
        this.outcomeService = outcomeService;
        this.mutationCreationMapper = mutationCreationMapper;
    }

    @Override
    public OutcomeCreationDTO toDto(Outcome outcome)
    {
        // No conversion to dto needed here
        return null;
    }

    @Override
    public Outcome toEntity(OutcomeCreationDTO outcomeCreationDTO)
    {
        Outcome outcome = outcomeCommonMapper.toEntity(outcomeCreationDTO.getOutcomeCommonDTO());
        if (outcome == null)
        {
            outcome = new Outcome();
        }
        String outcomeTypeName = outcomeCreationDTO.getOutcomeTypeName();
        OutcomeType outcomeType = outcomeService.getOutcomeTypeByNameFailingWhenNull(outcomeTypeName);
        outcome.setOutcomeType(outcomeType);
        setMutations(outcome, outcomeCreationDTO);
        return outcome;
    }

    private void setMutations(Outcome outcome, OutcomeCreationDTO outcomeCreationDTO)
    {
        List<MutationCreationDTO> mutationCreationDTOS = outcomeCreationDTO.getMutationCreationDTOS();
        Set<Mutation> mutations;
        if (mutationCreationDTOS != null)
        {
            mutations = new HashSet<>(mutationCreationMapper.toEntities(mutationCreationDTOS));
            mutations.forEach(outcome::addMutation);
        }
    }
}
