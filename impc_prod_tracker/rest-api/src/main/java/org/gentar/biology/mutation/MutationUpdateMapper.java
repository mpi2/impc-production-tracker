package org.gentar.biology.mutation;

import org.gentar.Mapper;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.gene.GeneService;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MutationUpdateMapper implements Mapper<Mutation, MutationUpdateDTO>
{
    private MutationCommonMapper mutationCommonMapper;
    private GeneService geneService;

    public MutationUpdateMapper(MutationCommonMapper mutationCommonMapper, GeneService geneService)
    {
        this.mutationCommonMapper = mutationCommonMapper;
        this.geneService = geneService;
    }

    @Override
    public MutationUpdateDTO toDto(Mutation mutation)
    {
        return null;
    }

    @Override
    public Mutation toEntity(MutationUpdateDTO mutationUpdateDTO)
    {
        Mutation mutation = new Mutation();
        if (mutationUpdateDTO.getMutationCommonDTO() != null)
        {
            mutation = mutationCommonMapper.toEntity(mutationUpdateDTO.getMutationCommonDTO());
        }
        addGenes(mutation, mutationUpdateDTO);
        return mutation;
    }

    private void addGenes(Mutation mutation, MutationUpdateDTO mutationUpdateDTO)
    {
        Set<Gene> genes = new HashSet<>();
        List<String> accessionIdsOrSymbols = mutationUpdateDTO.getSymbolOrAccessionIds();
        if (accessionIdsOrSymbols != null)
        {
            accessionIdsOrSymbols.forEach(x -> {
                Gene gene = geneService.findAndCreateInLocalIfNeeded(x);
                if (gene != null)
                {
                    genes.add(gene);
                }
            });
        }
        mutation.setGenes(genes);
    }
}
