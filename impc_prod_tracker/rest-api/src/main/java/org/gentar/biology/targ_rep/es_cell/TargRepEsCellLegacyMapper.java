package org.gentar.biology.targ_rep.es_cell;

import org.springframework.stereotype.Component;

/**
 * TargRepEsCellMapper.
 */
@Component
public class TargRepEsCellLegacyMapper{


    public TargRepEsCellLegacyResponseDTO toDto(TargRepEsCell entity) {
        TargRepEsCellLegacyResponseDTO esCellDto = new TargRepEsCellLegacyResponseDTO();
        if (entity != null) {
            esCellDto.setName(entity.getName());
            esCellDto.setPipelineName(entity.getPipeline().getName());
        }
        return esCellDto;
    }

}
