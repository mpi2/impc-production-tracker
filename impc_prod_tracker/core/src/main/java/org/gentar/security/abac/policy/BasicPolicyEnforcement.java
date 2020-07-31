/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package org.gentar.security.abac.policy;

import org.gentar.exceptions.SystemOperationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class BasicPolicyEnforcement implements PolicyEnforcement
{
    private static final Logger logger = LoggerFactory.getLogger(BasicPolicyEnforcement.class);

    @Qualifier("jsonFilePolicyDefinition")
    private PolicyDefinition policyDefinition;

    public BasicPolicyEnforcement(PolicyDefinition policyDefinition)
    {
        this.policyDefinition = policyDefinition;
    }

    @Override
    public boolean check(Object subject, Object resource, Object action, Object environment)
    {
        //Get all policy rules
        List<PolicyRule> allRules = policyDefinition.getAllPolicyRules();
        //Wrap the context
        SecurityAccessContext cxt = new SecurityAccessContext(subject, resource, action, environment);
        //Filter the rules according to context.
        List<PolicyRule> matchedRules = filterRules(allRules, cxt);
        //finally, check if any of the rules are satisfied, otherwise return false.
        return checkRules(matchedRules, cxt);
    }

    private List<PolicyRule> filterRules(List<PolicyRule> allRules, SecurityAccessContext cxt)
    {
        List<PolicyRule> matchedRules = new ArrayList<>();
        for(PolicyRule rule : allRules)
        {
            if (evaluateRuleExpression(rule.getTarget(), cxt))
            {
                matchedRules.add(rule);
            }
        }
        return matchedRules;
    }

    private boolean checkRules(List<PolicyRule> matchedRules, SecurityAccessContext cxt)
    {
        boolean result = false;

        for (PolicyRule rule : matchedRules)
        {
            if (evaluateRuleExpression(rule.getCondition(), cxt))
            {
                result = true;
//                logger.info("Found rule that evaluated true: [{}]", rule.getDescription());
                break;
            }
        }
        return result;
    }

    private boolean evaluateRuleExpression(Expression ruleExpression, SecurityAccessContext cxt)
    {
        Boolean result;
        try
        {
            result = ruleExpression.getValue(cxt, Boolean.class);
        }
        catch(SpelEvaluationException evalExc)
        {
            String errorMessage = "Expression [%s] cannot be evaluated: Error: %s";
            errorMessage = String.format(
                errorMessage, ruleExpression.getExpressionString(), evalExc.getLocalizedMessage());
            logger.error(errorMessage);
            throw new SystemOperationFailedException(
                "Permissions cannot be evaluated for the resource you are trying to access.",
                errorMessage);
        }
        if (result == null)
        {
            logger.info("Error evaluating policy");
            throw new SystemOperationFailedException(
                "Error evaluating policy", ruleExpression.getExpressionString());
        }
        return result;
    }
}
