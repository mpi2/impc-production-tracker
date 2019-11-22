package org.gentar.web.mapping;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class EntityMapper
{
    private ModelMapper modelMapper;

    public EntityMapper(ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
    }

    public <T, G> G toTarget(T entity, Class<G> targetClass)
    {
        G target = null;

        if (entity != null)
        {
            target = modelMapper.map(entity, targetClass);
        }
        return target;
    }

    public <T, G> List<G> toTargets(Collection<T> entities, Class<G> targetClass)
    {
        List<G> targetList = new ArrayList<>();
        if (entities != null)
        {
            entities.forEach(entity -> targetList.add(toTarget(entity, targetClass)));
        }
        return targetList;
    }

}
