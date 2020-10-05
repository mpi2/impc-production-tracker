package org.gentar.biology.mutation;

import org.gentar.Mapper;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.gene.GeneUpdateDTO;
import org.gentar.biology.gene.mappers.GeneUpdateMapper;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MutationUpdateMapper implements Mapper<Mutation, MutationUpdateDTO>
{
    private final MutationCommonMapper mutationCommonMapper;
    private final GeneUpdateMapper geneUpdateMapper;

    public MutationUpdateMapper(
        MutationCommonMapper mutationCommonMapper, GeneUpdateMapper geneUpdateMapper)
    {
        this.mutationCommonMapper = mutationCommonMapper;
        this.geneUpdateMapper = geneUpdateMapper;
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
        List<GeneUpdateDTO> geneUpdateDTOS = mutationUpdateDTO.getGeneUpdateDTOS();
        Set<Gene> genes = new HashSet<>( geneUpdateMapper.toEntities(geneUpdateDTOS));
        mutation.setGenes(genes);
    }
}
