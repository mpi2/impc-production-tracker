package uk.ac.ebi.impc_prod_tracker.domain.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest implements Serializable {
    private String name;
    private String password;
    private String email;
    private String workUnitName;
    private Set<String> instituteName;
    private String roleName;
}
