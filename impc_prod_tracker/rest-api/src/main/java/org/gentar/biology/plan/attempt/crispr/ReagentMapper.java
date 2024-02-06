package org.gentar.biology.plan.attempt.crispr;

import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.crispr.reagent.Reagent;
import org.gentar.biology.plan.attempt.crispr.reagent.ReagentService;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class ReagentMapper implements Mapper<Reagent, String>
{
    private final ReagentService reagentService;

    private static final String REAGENT_NAME_NOT_FOUND_ERROR = "Reagent name '%s' does not exist.";

    public ReagentMapper (ReagentService reagentService) { this.reagentService = reagentService; }

    @Override
    public String toDto(Reagent entity) {
        String name = null;
        if (entity != null)
        {
            name = entity.getName();
        }
        return name;
    }

    @Override
    public Reagent toEntity(String name) {
        Reagent reagent = reagentService.getReagentByName(name);

        if (reagent == null)
        {
            throw new UserOperationFailedException(String.format(REAGENT_NAME_NOT_FOUND_ERROR, name));
        }

        return reagent;
    }
}
