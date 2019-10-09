package uk.ac.ebi.impc_prod_tracker.conf.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserOperationFailedException extends OperationFailedException
{
    private String debugMessage;
    private Throwable cause;
    private HttpStatus httpStatus;

    public UserOperationFailedException(Throwable exception)
    {
        super(exception.getMessage());
        this.cause = exception;
    }

    public UserOperationFailedException(String message)
    {
        super(message);
    }

    public UserOperationFailedException(String message, HttpStatus httpStatus)
    {
        super(message);
        this.httpStatus = httpStatus;
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
