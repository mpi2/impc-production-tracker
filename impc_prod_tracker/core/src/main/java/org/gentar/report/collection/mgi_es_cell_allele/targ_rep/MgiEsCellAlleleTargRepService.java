package org.gentar.report.collection.mgi_es_cell_allele.targ_rep;

import java.util.List;

public interface MgiEsCellAlleleTargRepService {

    /**
     *
     * @return a list of MgiEsCellAlleleTargRepProjection Spring database projections
     *
     */
    List<MgiEsCellAlleleTargRepProjection> getSelectedTargRepProjections(List<String> cloneList);


    /**
     *
     * @return a list of MgiESCellAlleleTargRepESCellCloneProjection Spring database projections
     *
     */
    List<MgiESCellAlleleTargRepESCellCloneProjection> getTargRepClones();
}
