package org.gentar.report.utils.genotype_primer;

import org.gentar.report.collection.mgi_crispr_allele.genotype_primer.MgiCrisprAlleleReportGenotypePrimerProjection;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MgiGenotypePrimerFormatHelperImpl implements MgiGenotypePrimerFormatHelper{
    @Override
    public String formatGenotypePrimerData(Set<MgiCrisprAlleleReportGenotypePrimerProjection> projections) {
        String result = "";

        if (projections != null) {
            result = projections
                    .stream()
                    .map(p -> formatGenotypePrimer(p))
                    .collect(Collectors.joining("**"));
        }
        result = '"' + result + '"';

        return result;
    }

    private String formatGenotypePrimer(MgiCrisprAlleleReportGenotypePrimerProjection projection) {

        String sequence = projection.getSequence() == null ? "" : projection.getSequence();
        String name = projection.getName() == null ? "" : projection.getName();


        return
                "name::" + name + "||" +
                "sequence::" + sequence;
    }
}
