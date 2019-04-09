package uk.ac.ebi.impc_prod_tracker.data.biology.hgnc_gene;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class HgncGene {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable=false)
    private Long id;

    private String hgnc_id;

    private String symbol;

    private String name;

    private String locus_group;

    private String locus_type;

    private String status;

    private String location;

    private String location_sortable;

    private String alias_symbol;

    @Column(columnDefinition = "TEXT")
    private String alias_name;

    private String prev_symbol;

    @Column(columnDefinition = "TEXT")
    private String prev_name;

    private String gene_family;

    private String gene_family_id;

    private LocalDateTime date_approved_reserved;

    private LocalDateTime date_symbol_changed;

    private LocalDateTime date_name_changed;

    private LocalDateTime date_modified;

    private Long entrez_id;

    private String ensembl_gene_id;

    private String vega_id;

    private String ucsc_id;

    private String ena;

    private String refseq_accession;

    private String ccds_id;

    private String uniprot_ids;

    private String pubmed_id;

    private String mgd_id;

    private String rgd_id;

    @Column(columnDefinition = "TEXT")
    private String lsdb;

    private String cosmic;

    private String omim_id;

    private String mirbase;

    private Long homeodb;

    private String snornabase;

    private String bioparadigms_slc;

    private Long orphanet;

    private String pseudogeneOrg;

    private String horde_id;

    private String merops;

    private String imgt;

    private String iuphar;

    private Long kznf_gene_catalog;

    private Long mamitTrnadb;

    private String cd;

    private String lncrnadb;

    private String enzyme_id;

    private String intermediate_filament_db;

    private String rna_central_ids;

    private String lncipedia;

    private String gtrnadb;

}
