package org.gentar.biology.outcome;

import org.apache.logging.log4j.util.Strings;
import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.ColonyRepository;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class ColonyValidator
{
    private static final String NULL_FIELD_ERROR = "[%s] cannot be null.";
    private static final String COLONY_NAME_ALREADY_EXISTS_ERROR = "The colony [%s] already exists.";

    private final ColonyRepository colonyRepository;

    public ColonyValidator(ColonyRepository colonyRepository)
    {
        this.colonyRepository = colonyRepository;
    }

    public void validateData(Colony colony)
    {
        if (Strings.isBlank(colony.getName()))
        {
            throw new UserOperationFailedException(String.format(NULL_FIELD_ERROR, "Colony name"));
        }
        if (colony.getStrain() == null)
        {
            throw new UserOperationFailedException(String.format(NULL_FIELD_ERROR, "Background Strain"));
        }
        validateNewColonyNameDoesNotExist(colony);
    }

    private void validateNewColonyNameDoesNotExist(Colony colony)
    {
        if (colony.getId() == null)
        {
            String colonyName = colony.getName();
            Colony existingColony = colonyRepository.findByName(colonyName);
            if (existingColony != null)
            {
                throw new UserOperationFailedException(String.format(
                    COLONY_NAME_ALREADY_EXISTS_ERROR, colonyName));
            }
        }
    }
}
