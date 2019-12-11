package org.gentar.exceptions;

import lombok.Getter;

@Getter
public class UserOperationFailedException extends OperationFailedException
{
    private String debugMessage;
    private Throwable cause;

    public UserOperationFailedException(Throwable exception)
    {
        super(exception.getMessage());
        this.cause = exception;
    }

    public UserOperationFailedException(String message)
    {
        super(message);
    }

    public UserOperationFailedException(String message, Throwable cause)
    {
        this(message);
        this.cause = cause;
        this.debugMessage = cause.getLocalizedMessage();
    }

    public UserOperationFailedException(String message, String debugMessage)
    {
        this(message);
        this.debugMessage = debugMessage;
    }
}
