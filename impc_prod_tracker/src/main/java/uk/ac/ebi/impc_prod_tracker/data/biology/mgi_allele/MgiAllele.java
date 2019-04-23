package uk.ac.ebi.impc_prod_tracker.data.biology.mgi_allele;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class MgiAllele {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable=false)
    private Long id;

    private String projectId;

    private String dbName;

    private String mgi_allele_id;

    private String allele_symbol;

    @Column(columnDefinition = "TEXT")
    private String allele_name;

    private String mgi_id;

    private String gene_symbol;

    @Column(columnDefinition = "TEXT")
    private String cellLineIds;
}
