package org.gentar.biology.outcome;

import org.gentar.Mapper;
import org.gentar.biology.mutation.MutationMapper;
import org.gentar.biology.outcome.type.OutcomeType;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class OutcomeCreationMapper implements Mapper<Outcome, OutcomeCreationDTO>
{
    private OutcomeCommonMapper outcomeCommonMapper;
    private OutcomeService outcomeService;
    private MutationMapper mutationMapper;

    public OutcomeCreationMapper(
        OutcomeCommonMapper outcomeCommonMapper,
        OutcomeService outcomeService,
        MutationMapper mutationMapper)
    {
        this.outcomeCommonMapper = outcomeCommonMapper;
        this.outcomeService = outcomeService;
        this.mutationMapper = mutationMapper;
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
        outcome.setMutations(
            new HashSet<>(mutationMapper.toEntities(outcomeCreationDTO.getMutationDTOS())));
        return outcome;
    }
}
