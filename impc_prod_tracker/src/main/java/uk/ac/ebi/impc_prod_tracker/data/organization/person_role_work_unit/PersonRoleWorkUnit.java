package uk.ac.ebi.impc_prod_tracker.data.organization.person_role_work_unit;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.organization.consortium.Consortium;
import uk.ac.ebi.impc_prod_tracker.data.organization.institute.Institute;
import uk.ac.ebi.impc_prod_tracker.data.organization.person.Person;
import uk.ac.ebi.impc_prod_tracker.data.organization.role.Role;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class PersonRoleWorkUnit extends BaseEntity implements Serializable {

    @Id
    @SequenceGenerator(name = "personRoleWorkUnitSeq", sequenceName = "PERSON_ROLE_WORK_UNIT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personRoleWorkUnitSeq")
    private Long id;

    @NotNull
    @ManyToOne
    private Person person;

    @NotNull
    @ManyToOne
    private WorkUnit workUnit;

    @NotNull
    @ManyToOne(targetEntity = Role.class)
    private Role role;

}
