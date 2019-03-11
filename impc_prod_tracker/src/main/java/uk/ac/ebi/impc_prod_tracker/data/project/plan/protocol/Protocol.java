package uk.ac.ebi.impc_prod_tracker.data.project.plan.protocol;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.project.plan.Plan;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class Protocol extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "protocolSeq", sequenceName = "PROTOCOL_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "protocolSeq")
    private Long id;

    private String name;

    private String description;

    @ManyToMany(mappedBy = "protocols")
    private Set<Plan> plans;

    @NotNull
    @ManyToOne(targetEntity= ProtocolType.class)
    private ProtocolType protocolType;
}
