package org.gentar.statemachine;

public interface ProcessEvent
{
    Class<? extends Processor> getNextStepProcessor();
    String getName();
    String getDescription();
    ProcessState getInitialState();
    ProcessState getEndState();
    boolean isTriggeredByUser();
    String getTriggerNote();
}
