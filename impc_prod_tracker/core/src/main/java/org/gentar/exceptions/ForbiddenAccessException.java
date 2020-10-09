package org.gentar.exceptions;

import org.apache.logging.log4j.util.Strings;
import org.gentar.security.permissions.Operations;

public class ForbiddenAccessException extends RuntimeException
{
    private static final String ERROR_TEMPLATE = "You do not have permission to %s the %s%s.";

    public ForbiddenAccessException(Operations operation, String resourceType, String resourceId)
    {
        this(getErrorMessage(operation, resourceType, resourceId, null));
    }

    public ForbiddenAccessException(Operations operation, String resourceType, String resourceId, String details)
    {
        this(getErrorMessage(operation, resourceType, resourceId, details));
    }

    public ForbiddenAccessException(String message)
    {
        super(message);
    }

    private static String getErrorMessage(Operations action, String resourceType, String resourceId, String details)
    {
        String finalResourceType = resourceType.toLowerCase();
        String finalResourceId = " " + resourceId;
        String description = "";
        switch (action)
        {
            case CREATE:
                description = String.format(ERROR_TEMPLATE, "create", finalResourceType, "");
                break;
            case READ:
                description = String.format(ERROR_TEMPLATE, "read", finalResourceType, finalResourceId);
                break;
            case UPDATE:
                description = String.format(ERROR_TEMPLATE, "update", finalResourceType, finalResourceId);
                break;
            case DELETE:
                description = String.format(ERROR_TEMPLATE, "delete", finalResourceType, finalResourceId);
                break;
        }
        if (!Strings.isBlank(details))
        {
            description += " " +details;
        }
        return description;
    }
}
