package org.gentar.report.collection.mgi_es_cell_allele.targ_rep;

import org.springframework.beans.factory.annotation.Value;

public interface MgiESCellAlleleTargRepESCellCloneProjection {

    @Value("#{target.esCellClone}")
    String getEsCellClone();

}
