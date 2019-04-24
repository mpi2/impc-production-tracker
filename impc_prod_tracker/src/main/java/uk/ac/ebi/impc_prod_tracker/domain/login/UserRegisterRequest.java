package uk.ac.ebi.impc_prod_tracker.domain.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest implements Serializable {
    @NotNull private String name;
    @NotNull private String password;
    @NotNull private String email;
    @NotNull private String workUnitName;
    @NotNull private Set<String> instituteName;
    @NotNull private String roleName;
}
