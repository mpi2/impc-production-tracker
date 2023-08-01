package org.gentar.biology.targ_rep.allele.DistributionQc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.gentar.biology.targ_rep.TargRepAlleleResponseDTO;
import org.gentar.biology.targ_rep.allele.TargRepAllele;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCell;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCellService;
import org.gentar.biology.targ_rep.mutation.TargRepEsCellMutation;
import org.gentar.biology.targ_rep.mutation.TargRepEsCellMutationService;
import org.springframework.stereotype.Component;

/**
 * DistributionQcMapper.
 */
@Component
public class MgiAlleleAccessionMapper {

    private final TargRepEsCellMutationService esCellMutationService;

    private final TargRepEsCellService esCellService;

    public MgiAlleleAccessionMapper(TargRepEsCellMutationService esCellMutationService,
                                    TargRepEsCellService esCellService) {
        this.esCellMutationService = esCellMutationService;
        this.esCellService = esCellService;
    }

    public TargRepAlleleResponseDTO mgiAlleleIdToDto(TargRepAllele allele,
                                                     TargRepAlleleResponseDTO targRepAlleleResponseDTO) {

        targRepAlleleResponseDTO.setMgiAccessionId(getMgiAlleleAccessionIdByEsCEllId(allele));

        return targRepAlleleResponseDTO;
    }


    private List<Long> getEsCEllIdByAllele(TargRepAllele allele) {
        return esCellService.getTargRepEscellByAlleleFailsIfNull(allele)
            .stream()
            .map(TargRepEsCell::getId)
            .filter(Objects::nonNull).collect(Collectors.toList());

    }


    private List<String> getMgiAlleleAccessionIdByEsCEllId(TargRepAllele allele) {
        List<Long> esCEllIds = getEsCEllIdByAllele(allele);
        List<String> mgiAlleleAccessionIds = new ArrayList<>();
        for (Long EsCellId : esCEllIds) {
            TargRepEsCellMutation targRepEsCellMutation =esCellMutationService.getTargRepEsCellMutationByEsCellId(EsCellId);
            if(targRepEsCellMutation!=null && !mgiAlleleAccessionIds.contains(targRepEsCellMutation.getMgiAlleleAccessionId())) {
                mgiAlleleAccessionIds
                    .add(targRepEsCellMutation.getMgiAlleleAccessionId());
            }
        }
        return mgiAlleleAccessionIds;
    }

}
