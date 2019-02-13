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
package uk.ac.ebi.impc_prod_tracker.conf.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import uk.ac.ebi.impc_prod_tracker.domain.ImitsUser;
import uk.ac.ebi.impc_prod_tracker.domain.ProductionCentre;
import uk.ac.ebi.impc_prod_tracker.domain.UserRole;
import java.util.ArrayList;

/**
 * Class to keep the information from a user who logs into the system. This class represents the Subject of the system,
 * when referring to an authorization mechanism.
 *
 * @author Mauricio Martinez
 */
public class SecurityUser extends User
{
    private ImitsUser user;

    public SecurityUser(String username, String password, ImitsUser user)
    {
        super(username, password, new ArrayList<GrantedAuthority>(0));
        this.user = user;
    }

    public UserRole getRole()
    {
        return this.user.getRole();
    }

    public ProductionCentre getProductionCentre()
    {
        return this.user.getProductionCentre();
    }

    public void setRole(UserRole role)
    {
        this.user.setRole(role);
    }

    public String name() {
        return super.getUsername();
    }

    @Override
    public String toString() {
        return "{username:" + getUsername() + ", password: [PROTECTED], enabled:" + isEnabled()
            + ", accountNonExpired:" + isAccountNonExpired() + ", accountNonLocked:" + isAccountNonLocked()
            + ", credentialsNonExpired:" + isCredentialsNonExpired() + ", project:"  + ", role:" + user.getRole()
            + "}";
    }
}
