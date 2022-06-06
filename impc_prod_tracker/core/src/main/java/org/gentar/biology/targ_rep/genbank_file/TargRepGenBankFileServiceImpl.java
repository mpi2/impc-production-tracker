package org.gentar.biology.targ_rep.genbank_file;

import org.gentar.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


/**
 * TargRepGenBankFileServiceImpl.
 */
@Component
public class TargRepGenBankFileServiceImpl implements TargRepGenBankFileService {
    private final TargRepGenBankFileRepository genBankFileRepository;
    private static final String TARG_REP_GEN_BANK_FILE_NOT_EXISTS_ERROR =
        "A targ rep GeneBankFile with the id [%s] does not exist.";

    public TargRepGenBankFileServiceImpl(
        TargRepGenBankFileRepository genBankFileRepository) {
        this.genBankFileRepository = genBankFileRepository;
    }


    @Override
    public TargRepGenbankFile getNotNullTargRepGenBankFileById(Long id) {
        TargRepGenbankFile targRepGenBankFile =
            genBankFileRepository.findTargRepGenBankFileById(id);
        if (targRepGenBankFile == null) {
            throw new NotFoundException(
                String.format(TARG_REP_GEN_BANK_FILE_NOT_EXISTS_ERROR, id));
        }
        return targRepGenBankFile;
    }

    @Override
    public Page<TargRepGenbankFile> getPageableTargRepGenBankFile(Pageable page) {
        return genBankFileRepository.findAll(page);
    }

}
