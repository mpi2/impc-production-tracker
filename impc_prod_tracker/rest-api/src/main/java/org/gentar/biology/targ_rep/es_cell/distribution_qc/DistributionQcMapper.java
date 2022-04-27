package org.gentar.biology.targ_rep.es_cell.distribution_qc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.gentar.biology.targ_rep.TargRepDistributionQcDTO;
import org.gentar.biology.targ_rep.TargRepEsCellDistributionCentreDTO;
import org.gentar.biology.targ_rep.TargRepEsCellResponseDTO;
import org.gentar.biology.targ_rep.distribution_qc.TargRepDistributionQc;
import org.gentar.biology.targ_rep.distribution_qc.TargRepDistributionQcService;
import org.gentar.biology.targ_rep.distribution_qc.distribution_centre.TargRepEsCellDistributionCentre;
import org.springframework.stereotype.Component;

/**
 * DistributionQcMapper.
 */
@Component
public class DistributionQcMapper {

    private final TargRepDistributionQcService distributionQcService;

    List<TargRepDistributionQcDTO> targRepDistributionQcDTOS=new ArrayList<>();
    public DistributionQcMapper(TargRepDistributionQcService distributionQcService) {
        this.distributionQcService = distributionQcService;
    }

    public TargRepEsCellResponseDTO toDistributionQcDto(TargRepEsCellResponseDTO esCellDto) {
        List<TargRepDistributionQc> targRepDistributionQcs =
            getTargRepDistributionQc(esCellDto);
        targRepDistributionQcDTOS.clear();
        targRepDistributionQcs.stream()
            .forEach(repDistributionQc -> setRepDistributionQc(esCellDto, repDistributionQc));

        esCellDto.setTargRepDistributionQcList(targRepDistributionQcDTOS);

        return esCellDto;
    }

    private void setRepDistributionQc(TargRepEsCellResponseDTO esCellDto,
                                      TargRepDistributionQc repDistributionQc) {
        TargRepEsCellDistributionCentreDTO targRepEsCellDistributionCentre =
            new TargRepEsCellDistributionCentreDTO();
        TargRepDistributionQcDTO targRepDistributionQc = new TargRepDistributionQcDTO();
        if (repDistributionQc.getEsCellDistributionCentre().getId() == 1) {
            targRepEsCellDistributionCentre.setId(1L);
            targRepEsCellDistributionCentre.setName("WTSI");
        } else if (repDistributionQc.getEsCellDistributionCentre().getId() == 2) {
            targRepEsCellDistributionCentre.setId(2L);
            targRepEsCellDistributionCentre.setName("KOMP");
        } else if (repDistributionQc.getEsCellDistributionCentre().getId() == 3) {
            targRepEsCellDistributionCentre.setId(3L);
            targRepEsCellDistributionCentre.setName("EUCOMM");
        }
        targRepDistributionQc.setId(repDistributionQc.getId());
        targRepDistributionQc.setKaryotypeLow(repDistributionQc.getKaryotypeLow());
        targRepDistributionQc.setKaryotypeHigh(repDistributionQc.getKaryotypeHigh());
        targRepDistributionQc.setCopyNumber(repDistributionQc.getCopyNumber());
        targRepDistributionQc.setFivePrimeSrPcr(repDistributionQc.getFivePrimeSrPcr());
        targRepDistributionQc.setFivePrimeLrPcr(repDistributionQc.getFivePrimeLrPcr());
        targRepDistributionQc.setThawing(repDistributionQc.getThawing());
        targRepDistributionQc.setThreePrimeSrPcr(repDistributionQc.getThreePrimeSrPcr());
        targRepDistributionQc.setFivePrimeLrPcr(repDistributionQc.getThreePrimeLrPcr());
        targRepDistributionQc.setLoa(repDistributionQc.getLoa());
        targRepDistributionQc.setLoxp(repDistributionQc.getLoxp());
        targRepDistributionQc.setLacz(repDistributionQc.getLacz());
        targRepDistributionQc.setChr1(repDistributionQc.getChr1());
        targRepDistributionQc.setChr8a(repDistributionQc.getChr8a());
        targRepDistributionQc.setChr8b(repDistributionQc.getChr8b());
        targRepDistributionQc.setChr11a(repDistributionQc.getChr11a());
        targRepDistributionQc.setChr11b(repDistributionQc.getChr11b());
        targRepDistributionQc.setChry(repDistributionQc.getChry());
        targRepDistributionQc.setEsCellDistributionCentre(targRepEsCellDistributionCentre);
        targRepDistributionQcDTOS.add(targRepDistributionQc);

    }

    private List<TargRepDistributionQc> getTargRepDistributionQc(
        TargRepEsCellResponseDTO esCellDto) {
        return distributionQcService.getTargRepDistributionQcByEsCellId(esCellDto.getId());
    }

}
