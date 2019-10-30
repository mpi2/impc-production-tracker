package uk.ac.ebi.impc_prod_tracker.data.biology.project_consortium;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.organization.consortium.Consortium;
import uk.ac.ebi.impc_prod_tracker.data.organization.institute.Institute;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class ProjectConsortium  {
    @Id
    @SequenceGenerator(name = "projectConsortiumSeq", sequenceName = "PROJECT_CONSORTIUM_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projectConsortiumSeq")
    private Long id;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Consortium consortium;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "project_consortium_institute",
            joinColumns = @JoinColumn(name = "project_consortium_id"),
            inverseJoinColumns = @JoinColumn(name = "institute_id"))
    private Set<Institute> institutes;
}
