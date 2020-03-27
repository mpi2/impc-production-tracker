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
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusService;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.Processor;
import org.springframework.stereotype.Component;

/**
 * Class with the logic to move a Plan back to "Micro-Injection in Process" after being aborted.
 */
@Component
public class PlanAbortReverserProcessor implements Processor
{
    private PlanStateSetter planStateSetter;

    public PlanAbortReverserProcessor(PlanStateSetter planStateSetter)
    {
        this.planStateSetter = planStateSetter;
    }

    @Override
    public ProcessData process(ProcessData data)
    {
        reverseAbortion((Plan)data);
        return data;
    }

    private boolean canRevertAbortion(Plan plan)
    {
        // Put here the needed validation before reverting an abortion.
        return true;
    }

    private void reverseAbortion(Plan plan)
    {
        if (canRevertAbortion(plan))
        {
            ProcessEvent processEvent = plan.getEvent();
            String statusName = processEvent.getEndState().getInternalName();
            planStateSetter.setStatusByName(plan, statusName);
        }
    }
}
