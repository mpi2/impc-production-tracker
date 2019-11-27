package org.gentar.exceptions;

import lombok.Getter;

/**
 * Exception caused by an error in the system rather than by an error in a operation done by the user.
 */
@Getter
public class SystemOperationFailedException extends OperationFailedException
{
    private static final String ERROR_MESSAGE =
        "An unexpected error has occurred in the system. Please contact the administrator " +
        "to check the details in the logs.";

    private String debugMessage;
    private String exceptionDetail;
    private Throwable cause;

    // Avoid instantiation.
    private SystemOperationFailedException()
    {
        super("");
    }

    public SystemOperationFailedException(String debugMessage, String exceptionDetail)
    {
        super(ERROR_MESSAGE);
        this.debugMessage = debugMessage;
        this.exceptionDetail = exceptionDetail;
    }

    public SystemOperationFailedException(Throwable cause)
    {
        super(ERROR_MESSAGE);
        this.cause = cause;
        this.exceptionDetail = cause.getMessage();
    }
}
