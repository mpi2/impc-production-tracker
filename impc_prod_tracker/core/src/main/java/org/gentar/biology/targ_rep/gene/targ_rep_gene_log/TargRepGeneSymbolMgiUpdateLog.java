package org.gentar.biology.targ_rep.gene.targ_rep_gene_log;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
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
