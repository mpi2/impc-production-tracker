package org.gentar.report.utils.nuclease;

import org.gentar.report.collection.mgi_crispr_allele.nuclease.MgiCrisprAlleleReportNucleaseProjection;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MgiNucleaseFormatHelperImpl implements MgiNucleaseFormatHelper{

    @Override
    public String formatNucleaseData(Set<MgiCrisprAlleleReportNucleaseProjection> nucleaseProjections) {
        String result = "";

        if (nucleaseProjections != null) {
            result = nucleaseProjections
                    .stream()
                    .map(p -> formatNuclease(p))
                    .collect(Collectors.joining("**"));
        }

        return result;
    }

    private String formatNuclease(MgiCrisprAlleleReportNucleaseProjection projection) {

        String nucleaseType = projection.getNucleaseType() == null ? "" : projection.getNucleaseType();
        String nucleaseClass = projection.getNucleaseClass() == null ? "" : projection.getNucleaseClass();

        return "type::" + nucleaseType + "||" + "class::" + nucleaseClass;
    }
}
