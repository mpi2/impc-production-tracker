package uk.ac.ebi.impc_prod_tracker.data.biology.mgi_phenotypic_allele;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class MgiPhenotypicAllele {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable=false)
    private Long id;

    private String mgi_allele_id;

    private String allele_symbol;

    @Column(columnDefinition = "TEXT")
    private String allele_name;

    private String type;

    private String allele_attribute;

    private String pubmed_id;

    private String mgi_id;

    private String gene_symbol;

    private String refseq_id;

    private String ensembl_id;

    @Column(columnDefinition = "TEXT")
    private String mpIds;

    @Column(columnDefinition = "TEXT")
    private Long synonyms;

    @Column(columnDefinition = "TEXT")
    private Long gene_name;


}
