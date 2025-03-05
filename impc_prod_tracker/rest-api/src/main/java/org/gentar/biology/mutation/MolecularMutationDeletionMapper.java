package org.gentar.biology.mutation;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.mutation.mutation_deletion.MolecularMutationDeletion;
import org.springframework.stereotype.Component;

@Component
public class MolecularMutationDeletionMapper implements Mapper<MolecularMutationDeletion, MolecularMutationDeletionDTO> {

    private final EntityMapper entityMapper;

    public MolecularMutationDeletionMapper(EntityMapper entityMapper) {
        this.entityMapper = entityMapper;
    }


    @Override
    public MolecularMutationDeletionDTO toDto(MolecularMutationDeletion entity) {
        return entityMapper.toTarget(entity, MolecularMutationDeletionDTO.class);
    }

    public MolecularMutationDeletion toEntity(MolecularMutationDeletionDTO molecularMutationDeletionDTO) {
        MolecularMutationDeletion molecularMutationDeletion = new MolecularMutationDeletion();
        molecularMutationDeletion.setId(molecularMutationDeletionDTO.getId());
        molecularMutationDeletion.setChr(molecularMutationDeletionDTO.getChr());
        molecularMutationDeletion.setStart(molecularMutationDeletionDTO.getStart());
        molecularMutationDeletion.setStop(molecularMutationDeletionDTO.getStop());
        molecularMutationDeletion.setSize(molecularMutationDeletionDTO.getSize());
        return molecularMutationDeletion;

    }
}
