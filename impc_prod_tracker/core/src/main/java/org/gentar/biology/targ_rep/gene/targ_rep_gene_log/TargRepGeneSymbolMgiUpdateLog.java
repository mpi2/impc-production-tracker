package org.gentar.biology.targ_rep.gene.targ_rep_gene_log;

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
public class TargRepGeneSymbolMgiUpdateLog extends BaseEntity {

    @Id
    @SequenceGenerator(name = "TargRepGeneSymbolMgiUpdateLogSeq", sequenceName = "TARG_REP_GENE_SYMBOL_MGI_UPDATE_LOG_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TargRepGeneSymbolMgiUpdateLogSeq")
    private Long id;
    private String new_mgi_id;
    private String old_mgi_id;
    private String new_symbol;
    private String old_symbol;
}
