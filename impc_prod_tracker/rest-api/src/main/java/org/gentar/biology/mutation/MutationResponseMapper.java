package org.gentar.biology.mutation;

import org.gentar.Mapper;
import org.gentar.biology.gene.GeneMapper;
import org.gentar.biology.mutation.symbolCalculator.AlleleSymbolCalculator;
import org.gentar.biology.mutation.symbolCalculator.CrisprAlleleSymbolCalculator;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.outcome.OutcomeController;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanController;
import org.gentar.biology.plan.PlanResponseDTO;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MutationResponseMapper implements Mapper<Mutation, MutationResponseDTO>
{
    private final MutationCommonMapper mutationCommonMapper;
    private final GeneMapper geneMapper;
    private final AlleleSymbolCalculator alleleSymbolCalculator;

    public MutationResponseMapper(MutationCommonMapper mutationCommonMapper, GeneMapper geneMapper)
    {
        this.mutationCommonMapper = mutationCommonMapper;
        this.geneMapper = geneMapper;
        // Currently only taking into account crispr data.
        alleleSymbolCalculator = new CrisprAlleleSymbolCalculator();
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
        mutationResponseDTO.setMgiAlleleSymbol(mutation.getMgiAlleleSymbol());
        mutationResponseDTO.setMgiAlleleSymbolWithoutImpcAbbreviation(
            mutation.getMgiAlleleSymbolWithoutImpcAbbreviation());
        mutationResponseDTO.setDescription(mutation.getDescription());
        mutationResponseDTO.setAutoDescription(mutation.getAutoDescription());
        mutationResponseDTO.setGeneDTOS(geneMapper.toDtos(mutation.getGenes()));
        addSelfLink(mutationResponseDTO, mutation);
        addOutcomesLinks(mutationResponseDTO, mutation);
        mutationResponseDTO.setCalculatedMgiAlleleSymbol(
            alleleSymbolCalculator.calculateAlleleSymbol(mutation));
        return mutationResponseDTO;
    }

    private void addSelfLink(MutationResponseDTO mutationResponseDTO, Mutation mutation)
    {
        List<Link> links = new ArrayList<>();
        Set<Outcome> outcomes = mutation.getOutcomes();
        if (outcomes != null)
        {
            outcomes.forEach(x ->
                links.add(linkTo(methodOn(MutationController.class)
                    .findMutationByMin(mutation.getMin()))
                    .withSelfRel()));
        }
        mutationResponseDTO.add(links);
    }

    private void addOutcomesLinks(MutationResponseDTO mutationResponseDTO, Mutation mutation)
    {
        List<Link> links = new ArrayList<>();
        Set<Outcome> outcomes = mutation.getOutcomes();
        if (outcomes != null)
        {
            outcomes.forEach(x ->
                links.add(linkTo(methodOn(OutcomeController.class)
                    .findOneByPlanAndTpo(x.getPlan().getPin(), x.getTpo()))
                    .withRel("outcomes")));
        }
        mutationResponseDTO.add(links);
    }

    @Override
    public Mutation toEntity(MutationResponseDTO mutationResponseDTO)
    {
        // We don't have to make an entity from a response.
        return null;
    }
}
