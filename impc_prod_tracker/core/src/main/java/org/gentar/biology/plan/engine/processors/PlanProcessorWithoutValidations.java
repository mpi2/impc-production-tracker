/******************************************************************************
 Copyright 2020 EMBL - European Bioinformatics Institute

 Licensed under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 either express or implied. See the License for the specific
 language governing permissions and limitations under the
 License.
 */
package org.gentar.biology.plan.engine.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

/**
 * Abort a plan when no validations are needed
 */
@Component
public class PlanProcessorWithoutValidations extends AbstractProcessor
{
    public PlanProcessorWithoutValidations(PlanStateSetter planStateSetter)
    {
        super(planStateSetter);
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);
        boolean canExecuteTransition = canExecuteTransition((Plan)data);
        transitionEvaluation.setExecutable(canExecuteTransition);
        if (!canExecuteTransition)
        {
            transitionEvaluation.setNote(
                "Transition cannot be executed");
        }
        return transitionEvaluation;
    }

    public boolean canExecuteTransition(Plan plan)
    {
        return true;
    }
}
