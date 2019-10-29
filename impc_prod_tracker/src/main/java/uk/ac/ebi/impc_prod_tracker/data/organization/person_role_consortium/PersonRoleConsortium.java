package uk.ac.ebi.impc_prod_tracker.data.organization.person_role_consortium;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.organization.consortium.Consortium;
import uk.ac.ebi.impc_prod_tracker.data.organization.institute.Institute;
import uk.ac.ebi.impc_prod_tracker.data.organization.person.Person;
import uk.ac.ebi.impc_prod_tracker.data.organization.role.Role;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class PersonRoleConsortium extends BaseEntity implements Serializable {

    @Id
    @SequenceGenerator(name = "personRoleConsortiumSeq", sequenceName = "PERSON_ROLE_CONSORTIUM_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personRoleConsortiumSeq")
    private Long id;

    @NotNull
    @ManyToOne(targetEntity = Person.class)
    private Person person;

    @NotNull
    @ManyToOne(targetEntity = Consortium.class)
    private Consortium consortium;

    @NotNull
    @ManyToOne(targetEntity = Role.class)
    private Role role;
}
