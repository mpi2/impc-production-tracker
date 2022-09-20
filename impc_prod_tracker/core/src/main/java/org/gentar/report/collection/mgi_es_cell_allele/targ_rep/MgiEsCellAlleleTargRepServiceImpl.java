package org.gentar.report.collection.mgi_es_cell_allele.targ_rep;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
class MgiEsCellAlleleTargRepServiceImpl implements MgiEsCellAlleleTargRepService {

    private final MgiEsCellAlleleTargRepRepository mgiEsCellAlleleTargRepRepository;

    MgiEsCellAlleleTargRepServiceImpl(MgiEsCellAlleleTargRepRepository mgiEsCellAlleleTargRepRepository) {
        this.mgiEsCellAlleleTargRepRepository = mgiEsCellAlleleTargRepRepository;
    }

    @Override
    public List<MgiEsCellAlleleTargRepProjection> getSelectedTargRepProjections(List<String> cloneList) {
        return mgiEsCellAlleleTargRepRepository.findSelectedTargRepProjections(cloneList).stream()
                .collect(Collectors.toList());
    }

    @Override
    public List<MgiESCellAlleleTargRepESCellCloneProjection> getTargRepClones() {
        return mgiEsCellAlleleTargRepRepository.findAllTargRepESCellCloneProjections().stream()
                .collect(Collectors.toList());
    }

}
