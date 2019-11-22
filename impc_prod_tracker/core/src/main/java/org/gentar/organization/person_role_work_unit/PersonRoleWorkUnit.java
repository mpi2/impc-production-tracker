package org.gentar.organization.person_role_work_unit;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.organization.person.Person;
import org.gentar.organization.role.Role;
import org.gentar.organization.work_unit.WorkUnit;

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
