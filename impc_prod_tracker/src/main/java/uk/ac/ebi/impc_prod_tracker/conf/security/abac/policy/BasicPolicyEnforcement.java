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
package uk.ac.ebi.impc_prod_tracker.conf.security.abac.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.expression.EvaluationException;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.security.AapSystemSubject;
import uk.ac.ebi.impc_prod_tracker.conf.security.SecurityUser;
import uk.ac.ebi.impc_prod_tracker.conf.security.SystemSubject;
import uk.ac.ebi.impc_prod_tracker.domain.ImitsUser;
import uk.ac.ebi.impc_prod_tracker.domain.UserRole;
import java.util.ArrayList;
import java.util.List;

@Component
public class BasicPolicyEnforcement implements PolicyEnforcement
{
    private static final Logger logger = LoggerFactory.getLogger(BasicPolicyEnforcement.class);

    @Autowired
    @Qualifier("jsonFilePolicyDefinition")
    private PolicyDefinition policyDefinition;

    @Override
    public boolean check(Object subject, Object resource, Object action, Object environment)
    {
        Object subjectToValidate = subject;
        //Get all policy rules
        List<PolicyRule> allRules = policyDefinition.getAllPolicyRules();
        //Wrap the context
        if (subject.toString().equals("anonymousUser"))
        {
            subjectToValidate = getAnonymousSecurityUser();
        }
        SecurityAccessContext cxt = new SecurityAccessContext(subjectToValidate, resource, action, environment);
        //Filter the rules according to context.
        List<PolicyRule> matchedRules = filterRules(allRules, cxt);
        //finally, check if any of the rules are satisfied, otherwise return false.
        return checkRules(matchedRules, cxt);
    }

    private List<PolicyRule> filterRules(List<PolicyRule> allRules, SecurityAccessContext cxt)
    {
        List<PolicyRule> matchedRules = new ArrayList<>();
        for(PolicyRule rule : allRules) {
            try {
                if (rule.getTarget().getValue(cxt, Boolean.class))
                {
                    matchedRules.add(rule);
                }
            }
            catch(EvaluationException ex)
            {
                logger.info("An error occurred while evaluating PolicyRule.", ex);
            }
        }
        return matchedRules;
    }

    private boolean checkRules(List<PolicyRule> matchedRules, SecurityAccessContext cxt)
    {
        for(PolicyRule rule : matchedRules)
        {
            try
            {
                if (rule.getCondition().getValue(cxt, Boolean.class))
                {
                    return true;
                }
            }
            catch(EvaluationException ex)
            {
                logger.info("An error occurred while evaluating PolicyRule.", ex);
            }
        }
        return false;
    }

    private SystemSubject getAnonymousSecurityUser()
    {
        return new AapSystemSubject("anonymousUser");
    }
}
