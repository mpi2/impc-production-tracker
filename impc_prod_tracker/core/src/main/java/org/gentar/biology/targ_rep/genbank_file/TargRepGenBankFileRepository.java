package org.gentar.biology.targ_rep.genbank_file;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * TargRepGenbankFileRepository.
 */
public interface TargRepGenBankFileRepository extends
    PagingAndSortingRepository<TargRepGenbankFile, Long> {

    List<TargRepGenbankFile> findAll();

    TargRepGenbankFile findTargRepGenBankFileById(Long id);
}
