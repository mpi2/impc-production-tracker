package org.gentar.biology.plan.attempt.crispr;

import org.biojava.bio.seq.DNATools;
import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.biojava.bio.symbol.SymbolList;
import org.gentar.wge.WgeCrisprSearchConsumer;
import org.gentar.wge.WgeExonConsumer;
import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WgeMapper
{
    private final WgeExonConsumer wgeExonConsumer;
    private final WgeCrisprSearchConsumer wgeCrisprSearchConsumer;

    public WgeMapper(WgeExonConsumer wgeExonConsumer, WgeCrisprSearchConsumer wgeCrisprSearchConsumer) {
        this.wgeExonConsumer = wgeExonConsumer;
        this.wgeCrisprSearchConsumer = wgeCrisprSearchConsumer;
    }

    @Cacheable("exons_by_gene")
    public List<ExonDTO> getExonsByMarkerSymbol(String marker_symbol)
    {
        return getExonListFromSangerCrisprFinger(marker_symbol);
    }

    private List<ExonDTO> getExonListFromSangerCrisprFinger(String marker_symbol)
    {
        String result = wgeExonConsumer.executeQuery(marker_symbol);
        JSONObject json = new JSONObject(result);
        List<ExonDTO> exonDTOS = new ArrayList<>();

        if (!json.getJSONArray("exons").isEmpty())
        {
            json.getJSONArray("exons").forEach(x -> exonDTOS.add(getExon(x.toString())));
        }
        return exonDTOS;
    }

    private ExonDTO getExon(String x)
    {
        JSONObject json = new JSONObject(x);
        ExonDTO exonDTO = new ExonDTO();

        exonDTO.setId(json.getLong("id"));
        exonDTO.setExonId(json.getString("exon_id"));
        exonDTO.setGuideDetailsDTOS(getGuideSeqsByEnsemblId(json.getString("exon_id")));

        return exonDTO;
    }

    @Cacheable("guide_seqs_by_gene")
    public List<GuideDTO> getGuideSeqsByEnsemblId(String ensembl_id)
    {
        return getGuideSeqListFromSangerCrisprFinger(ensembl_id);
    }

    private List<GuideDTO> getGuideSeqListFromSangerCrisprFinger(String ensembl_id)
    {
        String result = wgeCrisprSearchConsumer.executeQuery(ensembl_id);
        JSONObject json = new JSONObject(result);
        List<GuideDTO> guideSeqs = new ArrayList<>();

        if (!json.getJSONArray(ensembl_id).isEmpty())
        {
            json.getJSONArray(ensembl_id).forEach(x -> guideSeqs.add(getGuideSeq(x.toString())));
        }
        return guideSeqs;
    }

    private GuideDTO getGuideSeq(String x)
    {
        JSONObject json = new JSONObject(x);
        GuideDTO guideDetailsDTO = new GuideDTO();

        guideDetailsDTO.setId(json.getLong("id"));
        guideDetailsDTO.setChr(json.getString("chr_name"));
        guideDetailsDTO.setStart(json.getInt("chr_start"));
        guideDetailsDTO.setStop(json.getInt("chr_end"));
        guideDetailsDTO.setTruncatedGuide(false);
        guideDetailsDTO.setEnsemblExonId(json.getString("ensembl_exon_id"));
        guideDetailsDTO.setGenomeBuild("GRCm38");
        guideDetailsDTO.setSangerService(true);

        if (json.getInt("pam_right") == 1) {
            guideDetailsDTO.setStrand("+");
        } else if (json.getInt("pam_right") == 0) {
            guideDetailsDTO.setStrand("-");
        }

        if (!json.getString("seq").startsWith("CC") && json.getString("seq").endsWith("GG"))
        {
            guideDetailsDTO.setSequence(json.getString("seq"));
            guideDetailsDTO.setReversed(false);
        }
        else if (json.getString("seq").startsWith("CC") && !json.getString("seq").endsWith("GG"))
        {
            reverseSequence(guideDetailsDTO, json.getString("seq"));
        }
        else if (json.getString("seq").startsWith("CC") && json.getString("seq").endsWith("GG"))
        {
            reverseSequenceByStrand(guideDetailsDTO, json.getString("seq"), guideDetailsDTO.getStrand());
        } else
        {
            // TODO ambiguous sequences
            guideDetailsDTO.setSequence(json.getString("seq"));
            guideDetailsDTO.setReversed(false);
        }

        String guideSequence = guideDetailsDTO.getSequence().substring(0, 20);
        String pam = guideDetailsDTO.getSequence().substring(20, 23);
        guideDetailsDTO.setGuideSequence(guideSequence);
        guideDetailsDTO.setPam(pam);

        return guideDetailsDTO;
    }

    private void reverseSequence(GuideDTO guideDetailsDTO, String sequence) {
        try {
            //make a DNA SymbolList
            SymbolList symL = DNATools.createDNA(sequence);

            //reverse complement it
            symL = DNATools.reverseComplement(symL);

            //prove that it worked
            guideDetailsDTO.setSequence(symL.seqString().toUpperCase());
            guideDetailsDTO.setReversed(true);
        }
        catch (IllegalSymbolException | IllegalAlphabetException ex) {
            //this will happen if you try and make the DNA  seq using non IUB symbols
            //or if you try and reverse complement a non DNA (RNA) sequence using DNATools (RNATools)
            ex.printStackTrace();
        }
    }

    private void reverseSequenceByStrand(GuideDTO guideDetailsDTO, String sequence, String strand)
    {
        if (strand.equals("-"))
        {
            reverseSequence(guideDetailsDTO, sequence);
        }
        else
        {
            guideDetailsDTO.setSequence(sequence);
            guideDetailsDTO.setReversed(false);
        }
    }
}
