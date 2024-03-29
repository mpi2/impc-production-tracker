package org.gentar.biology.gene.gene_log;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Data
@Entity
public class GeneSymbolMgiUpdateLog extends BaseEntity {

    @Id
    @SequenceGenerator(name = "geneSymbolMgiUpdateLogSeq", sequenceName = "GENE_SYMBOL_MGI_UPDATE_LOG_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "geneSymbolMgiUpdateLogSeq")
    private Long id;
    private String new_mgi_id;
    private String old_mgi_id;
    private String new_symbol;
    private String old_symbol;
    private String new_name;
    private String old_name;
}
