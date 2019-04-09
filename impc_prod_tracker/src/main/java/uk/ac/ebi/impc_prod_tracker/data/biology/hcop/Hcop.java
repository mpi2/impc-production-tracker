package uk.ac.ebi.impc_prod_tracker.data.biology.hcop;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class Hcop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable=false)
    private Long id;

    private Long human_entrez_gene;

    private String human_ensembl_gene;

    private String hgnc_id;

    private String human_name;

    private String human_symbol;

    private String human_chr;

    @Column(columnDefinition = "TEXT")
    private String human_assert_ids;

    private Long mouse_entrez_gene;

    private String mouse_ensembl_gene;

    private String mgi_id;

    private String mouse_name;

    private String mouse_symbol;

    private String mouse_chr;

    @Column(columnDefinition = "TEXT")
    private String mouse_assert_ids;

    @Column(columnDefinition = "TEXT")
    private String support;

}
