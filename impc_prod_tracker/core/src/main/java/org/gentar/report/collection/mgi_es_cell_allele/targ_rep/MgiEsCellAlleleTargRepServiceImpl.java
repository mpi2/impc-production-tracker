package org.gentar.report.collection.mgi_es_cell_allele.targ_rep;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
class MgiEsCellAlleleTargRepServiceImpl implements MgiEsCellAlleleTargRepService {

    private final MgiEsCellAlleleTargRepRepository mgiEsCellAlleleTargRepRepository;

    MgiEsCellAlleleTargRepServiceImpl(MgiEsCellAlleleTargRepRepository mgiEsCellAlleleTargRepRepository) {
        this.mgiEsCellAlleleTargRepRepository = mgiEsCellAlleleTargRepRepository;
    }

    @Override
    public List<MgiEsCellAlleleTargRepProjection> getTargRepProjections() {
        return mgiEsCellAlleleTargRepRepository.findAllTargRepProjections().stream()
                .peek(e -> System.out.println(e.getMgiAccessionId() + ": " + e.getCassetteEnd()))

//                .sorted(Comparator.comparing(MgiEsCellAlleleTargRepProjection::getMgiAccessionId))
                .collect(Collectors.toList());
    }
}
