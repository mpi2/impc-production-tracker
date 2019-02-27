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

import io.jsonwebtoken.Claims;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.domain.ProductionCentre;
import uk.ac.ebi.impc_prod_tracker.domain.UserRole;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of SystemSubject where most of the user information is taken from a token (jwt). Additional
 * information needs to be loaded from the database.
 * @author Mauricio Martinez
 */
@Component
@Data
@NoArgsConstructor
public class AapSystemSubject implements SystemSubject
{
    private Map<String, ProductionCentre> productionCentres = new HashMap<>();

    private String login;
    private String name;
    private String userRefId;
    private String email;
    private UserRole role;
    private ProductionCentre productionCentre;

    private void init()
    {
        ProductionCentre pc1 = new ProductionCentre(1, "PC1");
        ProductionCentre pc2 = new ProductionCentre(2, "PC2");
        ProductionCentre pc3 = new ProductionCentre(3, "PC3");
        productionCentres = new HashMap<>();

        productionCentres.put("newimitsadmin", pc1);
        productionCentres.put("newimitsuser1", pc1);
        productionCentres.put("newimitsuser2", pc2);
        productionCentres.put("newimitsuser3", pc3);
    }

    /**
     * Builds a AapSystemSubject object using the information in the claims of an already validated token (jwt).
     * @param claims Claims in the token with information about the user.
     * @return
     */
    public AapSystemSubject buildSystemSubjectByTokenInfo(Claims claims)
    {
        init();
        AapSystemSubject systemSubject = new AapSystemSubject();
        systemSubject.setLogin(claims.get("nickname", String.class));
        systemSubject.setName(claims.get("name", String.class));
        systemSubject.setUserRefId(claims.getSubject());
        systemSubject.setEmail(claims.get("email", String.class));
        systemSubject.setRole(UserRole.N0_ADMIN);
        systemSubject.setProductionCentre(loadProductionCentre(systemSubject.getLogin()));

        return systemSubject;
    }

    /**
     * Simple constructor that sets the minimal information for a user.
     * @param login
     */
    public AapSystemSubject(String login)
    {
        this.login = login;
        this.role = UserRole.N0_ADMIN;
    }

    @Override
    public ProductionCentre loadProductionCentre(String login)
    {
        return productionCentres.get(login);
    }
}
