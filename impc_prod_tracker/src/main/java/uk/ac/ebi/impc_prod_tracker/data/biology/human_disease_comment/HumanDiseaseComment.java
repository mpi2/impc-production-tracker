package uk.ac.ebi.impc_prod_tracker.data.biology.human_disease_comment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.human_disease.HumanDisease;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class HumanDiseaseComment extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "humanDiseaseCommentSeq", sequenceName = "HUMAN_DISEASE_COMMENT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "humanDiseaseCommentSeq")
    private Long id;

    private String text;

    @ManyToOne(targetEntity = HumanDisease.class)
    private HumanDisease humanDisease;

}

