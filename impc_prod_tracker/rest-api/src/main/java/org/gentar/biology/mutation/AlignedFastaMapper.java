package org.gentar.biology.mutation;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.mutation.aligned_fasta.AlignedFasta;
import org.gentar.biology.plan.attempt.crispr.AlignedFastaDTO;
import org.springframework.stereotype.Component;

@Component
public class AlignedFastaMapper implements Mapper<AlignedFasta, AlignedFastaDTO> {

    private final EntityMapper entityMapper;

    public AlignedFastaMapper(EntityMapper entityMapper) {
        this.entityMapper = entityMapper;
    }


    @Override
    public AlignedFastaDTO toDto(AlignedFasta entity) {
        return entityMapper.toTarget(entity, AlignedFastaDTO.class);
    }

    public AlignedFasta toEntity(AlignedFastaDTO alignedFastaDTO) {
        AlignedFasta alignedFasta = new AlignedFasta();
        alignedFasta.setId(alignedFastaDTO.getId());
        alignedFasta.setChrom(alignedFastaDTO.getChrom());
        alignedFasta.setChromStart(alignedFastaDTO.getChromStart());
        alignedFasta.setChromEnd(alignedFastaDTO.getChromEnd());
        alignedFasta.setName(alignedFastaDTO.getName());
        alignedFasta.setScore(alignedFastaDTO.getScore());
        alignedFasta.setStrand(alignedFastaDTO.getStrand());
        alignedFasta.setThickStart(alignedFastaDTO.getThickStart());
        alignedFasta.setThickEnd(alignedFastaDTO.getThickEnd());
        alignedFasta.setItemRgb(alignedFastaDTO.getItemRgb());
        alignedFasta.setBlockCount(alignedFastaDTO.getBlockCount());
        alignedFasta.setBlockSizes(alignedFastaDTO.getBlockSizes());
        alignedFasta.setBlockStarts(alignedFastaDTO.getBlockStarts());

        return alignedFasta;

    }
}
