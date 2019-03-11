package uk.ac.ebi.impc_prod_tracker.data.project.plan.protocol;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class ProtocolDetail extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "protocolDetailSeq", sequenceName = "PROTOCOL_DETAIL_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "protocolDetailSeq")
    private Long id;

    @NotNull
    @ManyToOne(targetEntity = Protocol.class)
    private Protocol protocol;

    private String protocolUrl;
}
