package org.gentar.biology.project.consortium;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.biology.project.Project;
import org.gentar.organization.consortium.Consortium;
import org.gentar.organization.institute.Institute;

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
