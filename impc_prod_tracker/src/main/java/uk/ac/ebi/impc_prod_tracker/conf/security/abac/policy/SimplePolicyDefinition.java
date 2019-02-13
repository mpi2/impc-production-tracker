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

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component("simplePolicyDefinition")
public class SimplePolicyDefinition
{
    private List<PolicyRule> rules;

    @PostConstruct
    private void init(){
        ExpressionParser exp = new SpelExpressionParser();
        rules = new ArrayList<>();

        PolicyRule newRule = new PolicyRule();
        newRule.setName("ResourceOwner");
        newRule.setDescription("Resource owner should have access to it.");
        newRule.setCondition(exp.parseExpression("true"));
        newRule.setTarget(exp.parseExpression("subject.name == resource.owner"));
        rules.add(newRule);
    }
    public List<PolicyRule> getAllPolicyRules() {
        return rules;
    }
}
