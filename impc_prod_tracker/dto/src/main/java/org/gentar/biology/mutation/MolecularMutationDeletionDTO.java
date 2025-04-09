package org.gentar.biology.mutation;

import lombok.Data;

@Data
public class MolecularMutationDeletionDTO
{
    private Long id;

    private String chr;

    private Integer start;

    private Integer stop;

    private Long size;
}
