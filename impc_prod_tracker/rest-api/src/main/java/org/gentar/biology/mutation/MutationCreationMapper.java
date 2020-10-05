package org.gentar.biology.mutation;

import org.gentar.Mapper;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.gene.GeneCreationDTO;
import org.gentar.biology.gene.mappers.GeneCreationMapper;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MutationCreationMapper implements Mapper<Mutation, MutationCreationDTO>
{
    private final MutationCommonMapper mutationCommonMapper;
    private final GeneCreationMapper geneCreationMapper;

    public MutationCreationMapper(
        MutationCommonMapper mutationCommonMapper, GeneCreationMapper geneCreationMapper)
    {
        this.mutationCommonMapper = mutationCommonMapper;
        this.geneCreationMapper = geneCreationMapper;
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
        List<GeneCreationDTO> geneCreationDTOS = mutationCreationDTO.getGeneCreationDTOS();
        Set<Gene> genes = new HashSet<>( geneCreationMapper.toEntities(geneCreationDTOS));
        mutation.setGenes(genes);
    }
}
