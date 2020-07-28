package org.gentar.biology.outcome;

import org.gentar.Mapper;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.mutation.MutationController;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OutcomeResponseMapper implements Mapper<Outcome, OutcomeResponseDTO>
{
    private final OutcomeCommonMapper outcomeCommonMapper;

    public OutcomeResponseMapper(OutcomeCommonMapper outcomeCommonMapper)
    {
        this.outcomeCommonMapper = outcomeCommonMapper;
    }

    @Override
    public OutcomeResponseDTO toDto(Outcome outcome)
    {
        OutcomeResponseDTO outcomeResponseDTO = new OutcomeResponseDTO();
        if (outcome != null)
        {
            OutcomeCommonDTO outcomeCommonDTO = outcomeCommonMapper.toDto(outcome);
            outcomeResponseDTO.setOutcomeCommonDTO(outcomeCommonDTO);
            if (outcome.getPlan() != null)
            {
                outcomeResponseDTO.setPin(outcome.getPlan().getPin());
            }

            outcomeResponseDTO.setTpo(outcome.getTpo());
            if (outcome.getOutcomeType() != null)
            {
                outcomeResponseDTO.setOutcomeTypeName(outcome.getOutcomeType().getName());
            }
        }
        addSelfLink(outcomeResponseDTO, outcome);
        addMutationLinks(outcomeResponseDTO, outcome);
        return outcomeResponseDTO;
    }

    private void addSelfLink(OutcomeResponseDTO outcomeResponseDTO, Outcome outcome)
    {
        Link link = linkTo(
            methodOn(OutcomeController.class).findOneByPlanAndTpo(
                outcome.getPlan().getPin(), outcome.getTpo()))
            .withSelfRel();
        outcomeResponseDTO.add(link);
    }

    private void addMutationLinks(OutcomeResponseDTO outcomeResponseDTO, Outcome outcome)
    {
       List<Link> links = new ArrayList<>();
        Set<Mutation> mutations = outcome.getMutations();
        if (mutations != null)
        {
            mutations.forEach(x ->
                links.add(linkTo(methodOn(MutationController.class)
                    .findMutationInOutcomeById(outcome.getPlan().getPin(), outcome.getTpo(), x.getMin()))
                    .withRel("mutations")));
        }
        outcomeResponseDTO.add(links);
    }

    @Override
    public Outcome toEntity(OutcomeResponseDTO outcomeResponseDTO)
    {
        // An Outcome response does not need to be converted to an entity.
        return null;
    }
}
