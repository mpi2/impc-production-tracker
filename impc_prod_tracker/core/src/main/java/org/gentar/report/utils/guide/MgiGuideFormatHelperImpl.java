package org.gentar.report.utils.guide;

import org.gentar.report.collection.mgi_crispr_allele.guide.MgiCrisprAlleleReportGuideProjection;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MgiGuideFormatHelperImpl implements MgiGuideFormatHelper{

    public String formatGuideData (Set<MgiCrisprAlleleReportGuideProjection> guideProjections){
        String result = "";

        if (guideProjections != null) {
            result = guideProjections
                    .stream()
                    .map(this::formatGuide)
                    .collect(Collectors.joining("**"));
        }

        return result;
    }

    private String formatGuide(MgiCrisprAlleleReportGuideProjection projection){

        String sequence = projection.getSequence() == null ? "" : projection.getSequence();
        String guideSeqeunce = projection.getGuideSequence() == null ? "" : projection.getGuideSequence();
        String pam = projection.getPam() == null ? "" : projection.getPam();
        String chr = projection.getChr() == null ? "" : projection.getChr();
        String start = projection.getStart() == null ? "" : projection.getStart().toString();
        String stop = projection.getStop() == null ? "" : projection.getStop().toString();
        String strand = projection.getStrand() == null ? "" : projection.getStrand();
        String genomeBuild = projection.getGenomeBuild() == null ? "" : projection.getGenomeBuild();


        return "sequence::" + sequence + "||" +
                        "guideSeqeunce::" + guideSeqeunce + "||" +
                        "PAM::" + pam  + "||" +
                        "chr::" + chr + "||" +
                        "start::" + start + "||" +
                        "stop::" + stop + "||" +
                        "strand::" + strand + "||" +
                        "genomeBuild::" + genomeBuild;
    }
}
