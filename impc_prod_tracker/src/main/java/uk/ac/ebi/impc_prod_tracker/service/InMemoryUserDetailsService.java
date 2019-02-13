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
package uk.ac.ebi.impc_prod_tracker.service;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.security.SecurityUser;
import uk.ac.ebi.impc_prod_tracker.domain.ImitsUser;
import uk.ac.ebi.impc_prod_tracker.domain.ProductionCentre;
import uk.ac.ebi.impc_prod_tracker.domain.UserRole;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component("InMemoryUserDetailsService")
public class InMemoryUserDetailsService implements UserDetailsService
{
    private Map<String, SecurityUser> users = new HashMap<>();

    public InMemoryUserDetailsService()
    {
    }

    @PostConstruct
    private void init() {
        ProductionCentre pc1 = new ProductionCentre(1, "PC1");
        ProductionCentre pc2 = new ProductionCentre(2, "PC2");
        ProductionCentre pc3 = new ProductionCentre(3, "PC3");
        this.users = new HashMap<>();
        users.put("admin", new SecurityUser("admin", "password", new ImitsUser("joe", UserRole.ADMIN, pc1)));
        users.put("pm1", new SecurityUser("pm1", "password", new ImitsUser("mary", UserRole.N0_ADMIN, pc2)));
        users.put("pm2", new SecurityUser("pm2", "password", new ImitsUser("Ana", UserRole.N0_ADMIN, pc3)));
        users.put("dev1", new SecurityUser("dev1", "password", new ImitsUser("John", UserRole.N0_ADMIN, pc1)));
        users.put("dev2", new SecurityUser("dev2", "password", new ImitsUser("Claus", UserRole.N0_ADMIN, pc2)));
        users.put("test1", new SecurityUser("test1", "password", new ImitsUser("Tom", UserRole.N0_ADMIN, pc1)));
        users.put("test2",new SecurityUser("test2", "password", new ImitsUser("Dan", UserRole.N0_ADMIN, pc3)));
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUser user = users.get(username.toLowerCase());

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return new SecurityUser(user.getUsername(), user.getPassword(), new ImitsUser(user.getUsername(), user.getRole(), user.getProductionCentre()));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
