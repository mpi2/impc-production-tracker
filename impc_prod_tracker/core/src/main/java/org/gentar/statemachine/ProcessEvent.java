package org.gentar.statemachine;

public interface ProcessEvent
{
    Class<? extends Processor> nextStepProcessor();
    String getName();
    String getDescription();
    ProcessState getInitialState();
    ProcessState getEndState();
    boolean isTriggeredByUser();
    String getTriggerNote();
}
