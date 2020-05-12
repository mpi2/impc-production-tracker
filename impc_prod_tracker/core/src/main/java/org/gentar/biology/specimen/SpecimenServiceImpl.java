package org.gentar.biology.specimen;

import org.gentar.biology.specimen.type.SpecimenType;
import org.gentar.biology.specimen.type.SpecimenTypeRepository;
import org.gentar.statemachine.ProcessEvent;
import org.springframework.stereotype.Component;

@Component
public class SpecimenServiceImpl implements SpecimenService
{
    private SpecimenTypeRepository specimenTypeRepository;
    private SpecimenStateMachineResolver specimenStateMachineResolver;

    public SpecimenServiceImpl(
        SpecimenTypeRepository specimenTypeRepository,
        SpecimenStateMachineResolver specimenStateMachineResolver)
    {
        this.specimenTypeRepository = specimenTypeRepository;
        this.specimenStateMachineResolver = specimenStateMachineResolver;
    }

    public SpecimenType getSpecimenTypeByName(String name)
    {
        return specimenTypeRepository.findByNameIgnoreCase(name);
    }

    @Override
    public ProcessEvent getProcessEventByName(Specimen specimen, String name)
    {
        return specimenStateMachineResolver.getProcessEventByActionName(specimen, name);
    }
}
