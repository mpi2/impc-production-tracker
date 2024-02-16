package org.gentar.biology.plan.attempt.crispr.reagent;

import org.springframework.stereotype.Component;

@Component
public class ReagentServiceImpl implements ReagentService
{
    private final ReagentRepository reagentRepository;

    public ReagentServiceImpl (ReagentRepository reagentRepository) { this.reagentRepository = reagentRepository; }

    @Override
    public Reagent getReagentByName (String name) { return reagentRepository.findByNameIgnoreCase(name); }
}
