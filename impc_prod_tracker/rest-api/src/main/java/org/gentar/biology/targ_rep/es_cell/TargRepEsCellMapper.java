package org.gentar.biology.targ_rep.es_cell;

import org.gentar.Mapper;
import org.gentar.biology.targ_rep.TargRepEsCellDTO;
import org.springframework.stereotype.Component;

@Component
public class TargRepEsCellMapper  implements Mapper<TargRepEsCell, TargRepEsCellDTO>
{

    public TargRepEsCellMapper () {

    }

    @Override
    public TargRepEsCellDTO toDto(TargRepEsCell entity)
    {
        TargRepEsCellDTO esCellDTO = new TargRepEsCellDTO();
        if (entity != null)
        {
            esCellDTO.setId(entity.getId());
            esCellDTO.setName(entity.getName());
        }
        return esCellDTO;
    }

    @Override
    public TargRepEsCell toEntity(TargRepEsCellDTO dto) {
        return Mapper.super.toEntity(dto);
    }

}
