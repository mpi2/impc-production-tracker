package uk.ac.ebi.impc_prod_tracker.data.biology.mgi_disease;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class MgiDisease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable=false)
    private Long id;

    private String doID;
    private String diseaseName;

    @Column(columnDefinition = "TEXT")
    private String omimIds;
    private Long  homologeneId;
    private String organismName;
    private Long  taxonId;
    private String symbol;
    private Long  entrezId;
    private String mgiId;

}
