package uk.ac.ebi.impc_prod_tracker.data.organization.project_consortium_institute;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.organization.institute.Institute;
import uk.ac.ebi.impc_prod_tracker.data.organization.person_role_consortium.PersonRoleConsortium;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class ProjectConsortiumInstitute {
    @Id
    @SequenceGenerator(name = "personRoleConsortiumSeq", sequenceName = "PERSON_ROLE_CONSORTIUM_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personRoleConsortiumSeq")
    private Long id;

    @NotNull
    @ManyToOne(targetEntity = PersonRoleConsortium.class)
    private PersonRoleConsortium personRoleConsortium;

    @NotNull
    @ManyToOne(targetEntity = Institute.class)
    private Institute institute;
}
