package org.gentar.organization.funder;

import org.gentar.organization.work_group.WorkGroupRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FunderServiceImpl implements FunderService
{
    private final FunderRepository funderRepository;
    private final WorkGroupRepository workGroupRepository;

    public FunderServiceImpl(FunderRepository funderRepository, WorkGroupRepository workGroupRepository)
    {
        this.funderRepository = funderRepository;
        this.workGroupRepository = workGroupRepository;
    }

    @Override
    @Cacheable("funders")
    public Funder getFunderByName(String name)
    {
        return funderRepository.getFirstByNameIgnoreCase(name);
    }

    @Override
    public Map<String, List<String>> getFunderNamesByWorkGroupNamesMap()
    {
        Map<String, List<String>> map = new HashMap<>();
        var workGroups = workGroupRepository.findAll();
        workGroups.forEach(workUnit -> {
            var funders = workUnit.getFunders();
            List<String> funderNames = new ArrayList<>();
            if (funders != null)
            {
                funders.forEach(funder -> funderNames.add(funder.getName()));
            }
            map.put(workUnit.getName(), funderNames);
        });
        return map;
    }

    @Override
    public List<Funder> getAll()
    {
        return funderRepository.findAll();
    }
}
