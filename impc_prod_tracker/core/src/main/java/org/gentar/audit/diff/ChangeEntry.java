package org.gentar.audit.diff;

import lombok.Data;

@Data
public class ChangeEntry
{
    private String property;
    private Object oldValue;
    private Object newValue;
    private Class<?> type;
    private ChangeType changeType;

    public String toString()
    {
        return property + " old["+oldValue+"] new[" + newValue + "] type[" + type.getSimpleName() +
            "] changeType[" + changeType + "]";
    }
}
