package uk.ac.ebi.impc_prod_tracker.data.biology.project_intention.type;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class IntentionType
{
    @Id
    @SequenceGenerator(name = "intentionTypeSeq", sequenceName = "INTENTION_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "intentionTypeSeq")
    private Long id;

    private String name;
}
