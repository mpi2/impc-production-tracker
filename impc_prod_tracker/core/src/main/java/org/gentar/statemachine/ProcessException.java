package org.gentar.statemachine;

import java.io.Serial;

public class ProcessException extends Exception
{
    @Serial
    private static final long serialVersionUID = 1L;

    public ProcessException(String message) {
        super(message);
    }

    public ProcessException(String message, Throwable e) {
        super(message, e);
    }
}
