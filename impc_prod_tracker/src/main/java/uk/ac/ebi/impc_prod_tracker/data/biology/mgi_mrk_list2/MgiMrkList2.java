package uk.ac.ebi.impc_prod_tracker.data.biology.mgi_mrk_list2;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class MgiMrkList2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable=false)
    private Long id;

    private String mgi_id;

    private String chr;

    private String cM;

    private String start;

    private String stop;

    private String strand;

    private String symbol;

    private String status;

    private String name;

    private String marker_type;

    private String feature_type;

    @Column(columnDefinition = "TEXT")
    private String synonyms;
}
