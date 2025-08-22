package org.gentar.biology.plan.attempt.crispr;

import org.gentar.biology.plan.attempt.crispr.guide.Guide;
import org.gentar.ensembl_assembly_mapping.AssemblyMapConsumer;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
public class AssemblyMapMapper
{
    private final AssemblyMapConsumer assemblyMapConsumer;

    public AssemblyMapMapper(AssemblyMapConsumer assemblyMapConsumer) {
        this.assemblyMapConsumer = assemblyMapConsumer;
    }

    public void assemblyConvertion(Guide guide)
    {
        String strand;
        if (guide.getStrand().equals("+"))
        {
            strand = "1";
        } else {
            strand = "-1";
        }
        String result = assemblyMapConsumer.executeQuery("chr"+guide.getChr(), guide.getStart(), guide.getStop());
        JSONObject json = new JSONObject(result);

        AssemblyDTO assemblyDTO = new AssemblyDTO();
        JSONObject mapped = assemblyMapConsumer.validMapping(json);
        if (mapped != null) {
            assemblyDTO = getAssembly(mapped, strand);
        }

        convertGuide(guide, assemblyDTO);
    }

    private void convertGuide(Guide guide, AssemblyDTO assemblyDTO)
    {
        guide.setStart(assemblyDTO.getStart());
        guide.setStop(assemblyDTO.getStop());
        guide.setStrand(assemblyDTO.getStrand());
        guide.setChr(assemblyDTO.getChr());
        guide.setGenomeBuild(assemblyDTO.getAssembly());
    }

    private AssemblyDTO getAssembly(JSONObject mapped, String strand)
    {
        AssemblyDTO assemblyDTO = new AssemblyDTO();
        JSONObject map = mapped.getJSONObject("mapped");

        assemblyDTO.setStart(map.getInt("start"));
        assemblyDTO.setChr(map.getString("chrom"));
        assemblyDTO.setStop(map.getInt("end"));
        assemblyDTO.setAssembly(map.getString("assembly"));
        if (Objects.equals(strand, "-1"))
        {
            assemblyDTO.setStrand("-");
        } else if (Objects.equals(strand, "1")) {
            assemblyDTO.setStrand("+");
        }

        return assemblyDTO;
    }
}
