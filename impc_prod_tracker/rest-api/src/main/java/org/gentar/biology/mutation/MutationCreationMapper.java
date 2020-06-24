package org.gentar.biology.mutation;

import org.gentar.Mapper;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.gene.GeneService;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MutationCreationMapper implements Mapper<Mutation, MutationCreationDTO>
{
    private MutationCommonMapper mutationCommonMapper;
    private GeneService geneService;

    public MutationCreationMapper(MutationCommonMapper mutationCommonMapper, GeneService geneService)
    {
        this.mutationCommonMapper = mutationCommonMapper;
        this.geneService = geneService;
    }

    @Override
    public MutationCreationDTO toDto(Mutation mutation)
    {
        return null;
    }

    @Override
    public Mutation toEntity(MutationCreationDTO mutationCreationDTO)
    {
        Mutation mutation = new Mutation();
        if (mutationCreationDTO.getMutationCommonDTO() != null)
        {
            mutation = mutationCommonMapper.toEntity(mutationCreationDTO.getMutationCommonDTO());
        }
        addGenes(mutation, mutationCreationDTO);
        return mutation;
    }

    private void addGenes(Mutation mutation, MutationCreationDTO mutationCreationDTO)
    {
        Set<Gene> genes = new HashSet<>();
        List<String> accessionIdsOrSymbols = mutationCreationDTO.getSymbolOrAccessionIds();
        if (accessionIdsOrSymbols != null)
        {
            accessionIdsOrSymbols.forEach(x -> {
                Gene gene = geneService.findAndCreateInLocalIfNeededFailIfNull(x);
                if (gene != null)
                {
                    genes.add(gene);
                }
            });
        }
        mutation.setGenes(genes);
    }
}
