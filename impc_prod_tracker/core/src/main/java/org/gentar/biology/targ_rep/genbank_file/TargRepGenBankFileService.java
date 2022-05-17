package org.gentar.biology.targ_rep.genbank_file;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * TargRepGenBankFileService.
 */
public interface TargRepGenBankFileService {

    TargRepGenbankFile getNotNullTargRepGenBankFileById(Long id);

    Page<TargRepGenbankFile> getPageableTargRepGenBankFile(Pageable page);
}
