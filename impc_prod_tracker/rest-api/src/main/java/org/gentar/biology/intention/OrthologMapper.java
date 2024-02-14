package org.gentar.biology.intention;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.ortholog.Ortholog;
import org.springframework.stereotype.Component;

@Component
public class OrthologMapper implements Mapper<Ortholog, OrthologDTO>
{
    private final EntityMapper entityMapper;

    public OrthologMapper(EntityMapper entityMapper)
    {
        this.entityMapper = entityMapper;
    }

    @Override
    public OrthologDTO toDto(Ortholog entity)
    {
        return entityMapper.toTarget(entity, OrthologDTO.class);
    }
}
