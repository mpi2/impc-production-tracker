package org.gentar.biology.plan.attempt.crispr.guide;

import org.gentar.biology.plan.attempt.crispr.guide.exons.GuidesSequenceDetailsDTO;
import org.gentar.wge.WgeCrisprSearchConsumer;
import org.json.*;
import org.gentar.biology.plan.attempt.crispr.guide.exons.ExonDTO;
import org.gentar.wge.WgeExonConsumer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GuideServiceImpl implements GuideService
{
    private final WgeExonConsumer wgeExonConsumer;
    private final WgeCrisprSearchConsumer wgeCrisprSearchConsumer;

    public GuideServiceImpl(WgeExonConsumer wgeExonConsumer, WgeCrisprSearchConsumer wgeCrisprSearchConsumer)
    {
        this.wgeExonConsumer = wgeExonConsumer;
        this.wgeCrisprSearchConsumer = wgeCrisprSearchConsumer;
    }

    @Override
    @Cacheable("exons_by_gene")
    public List<ExonDTO> getExonsByMarkerSymbol(String marker_symbol)
    {
        return getExonListFromSangerCrisprFinger(marker_symbol);
    }

    private List<ExonDTO> getExonListFromSangerCrisprFinger(String marker_symbol)
    {
        String result = wgeExonConsumer.executeQuery(marker_symbol);
        JSONObject json = new JSONObject(result);
        List<ExonDTO> exons = new ArrayList<>();

        if (!json.getJSONArray("exons").isEmpty())
        {
            json.getJSONArray("exons").forEach(x -> exons.add(getExon(x.toString())));
        }
        return exons;
    }

    private ExonDTO getExon(String x)
    {
        JSONObject json = new JSONObject(x);
        ExonDTO exonDTO = new ExonDTO();

        exonDTO.setId(json.getLong("id"));
        exonDTO.setExonId(json.getString("exon_id"));
        exonDTO.setGuidesSequenceDetailsDTOS(getGuideSeqsByEnsemblId(json.getString("exon_id")));

        return exonDTO;
    }

    @Override
    @Cacheable("guide_seqs_by_gene")
    public List<GuidesSequenceDetailsDTO> getGuideSeqsByEnsemblId(String ensembl_id)
    {
        return getGuideSeqListFromSangerCrisprFinger(ensembl_id);
    }

    private List<GuidesSequenceDetailsDTO> getGuideSeqListFromSangerCrisprFinger(String ensembl_id)
    {
        String result = wgeCrisprSearchConsumer.executeQuery(ensembl_id);
        JSONObject json = new JSONObject(result);
        List<GuidesSequenceDetailsDTO> guideSeqs = new ArrayList<>();

        if (!json.getJSONArray(ensembl_id).isEmpty())
        {
            json.getJSONArray(ensembl_id).forEach(x -> guideSeqs.add(getGuideSeq(x.toString())));
        }
        return guideSeqs;
    }

    private GuidesSequenceDetailsDTO getGuideSeq(String x)
    {
        JSONObject json = new JSONObject(x);
        GuidesSequenceDetailsDTO guidesSequenceDetailsDTO = new GuidesSequenceDetailsDTO();

        guidesSequenceDetailsDTO.setId(json.getLong("id"));
        guidesSequenceDetailsDTO.setPamRight(json.getInt("pam_right"));
        guidesSequenceDetailsDTO.setChrName(json.getString("chr_name"));
        guidesSequenceDetailsDTO.setChrStart(json.getInt("chr_start"));
        guidesSequenceDetailsDTO.setChrEnd(json.getInt("chr_end"));
        guidesSequenceDetailsDTO.setSeq(json.getString("seq"));

        return guidesSequenceDetailsDTO;
    }
}
