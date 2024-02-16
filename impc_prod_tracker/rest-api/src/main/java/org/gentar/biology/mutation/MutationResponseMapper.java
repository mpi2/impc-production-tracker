package org.gentar.biology.mutation;

import org.gentar.Mapper;
import org.gentar.biology.gene.mappers.GeneResponseMapper;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.outcome.OutcomeController;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MutationResponseMapper implements Mapper<Mutation, MutationResponseDTO>
{
    private final MutationCommonMapper mutationCommonMapper;
    private final GeneResponseMapper geneResponseMapper;

    public MutationResponseMapper(
        MutationCommonMapper mutationCommonMapper, GeneResponseMapper geneResponseMapper)
    {
        this.mutationCommonMapper = mutationCommonMapper;
        this.geneResponseMapper = geneResponseMapper;
    }

    @Override
    public MutationResponseDTO toDto(Mutation mutation)
    {
        MutationResponseDTO mutationResponseDTO = new MutationResponseDTO();
        MutationCommonDTO mutationCommonDTO = mutationCommonMapper.toDto(mutation);
        mutationResponseDTO.setMutationCommonDTO(mutationCommonDTO);
        mutationResponseDTO.setId(mutation.getId());
        mutationResponseDTO.setMin(mutation.getMin());
        mutationResponseDTO.setMgiAlleleId(mutation.getMgiAlleleId());
        mutationResponseDTO.setImitsAllele(mutation.getImitsAllele());
        mutationResponseDTO.setMgiAlleleSymbolWithoutImpcAbbreviation(
            mutation.getMgiAlleleSymbolWithoutImpcAbbreviation());
        mutationResponseDTO.setGeneDTOS(geneResponseMapper.toDtos(mutation.getGenes()));
        addSelfLink(mutationResponseDTO, mutation);
        addOutcomesLinks(mutationResponseDTO, mutation);
        return mutationResponseDTO;
    }

    private void addSelfLink(MutationResponseDTO mutationResponseDTO, Mutation mutation)
    {
        List<Link> links = new ArrayList<>();
        Set<Outcome> outcomes = mutation.getOutcomes();
        if (outcomes != null)
        {
            outcomes.forEach(x -> {
                Link link = linkTo(methodOn(MutationController.class)
                    .findMutationByMin(mutation.getMin()))
                    .withSelfRel();
                link = link.withHref(decode(link.getHref()));
                links.add(link);
            });
        }
        mutationResponseDTO.add(links);
    }

    private void addOutcomesLinks(MutationResponseDTO mutationResponseDTO, Mutation mutation)
    {
        List<Link> links = new ArrayList<>();
        Set<Outcome> outcomes = mutation.getOutcomes();
        if (outcomes != null)
        {
            outcomes.forEach(x -> {
                Link link = linkTo(methodOn(OutcomeController.class)
                    .findOneByPlanAndTpo(x.getPlan().getPin(), x.getTpo()))
                    .withRel("outcomes");
                link = link.withHref(decode(link.getHref()));
                links.add(link);
            });
        }
        mutationResponseDTO.add(links);
    }

    @Override
    public Mutation toEntity(MutationResponseDTO mutationResponseDTO)
    {
        // We don't have to make an entity from a response.
        return null;
    }

    private String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
