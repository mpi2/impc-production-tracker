package org.gentar.biology.plan.attempt.crispr;

import org.gentar.biology.plan.attempt.crispr.guide.Guide;
import org.gentar.ensembl_assembly_mapping.AssemblyMapConsumer;
import org.json.JSONObject;
import org.springframework.stereotype.Component;


@Component
public class AssemblyMapMapper
{
    private final AssemblyMapConsumer assemblyMapConsumer;

    public AssemblyMapMapper(AssemblyMapConsumer assemblyMapConsumer) {
        this.assemblyMapConsumer = assemblyMapConsumer;
    }

    public void assemblyConvertion(Guide guide)
    {
        String coordinates = guide.getChr() + ":" + guide.getStart() + ".." + guide.getStop();
        String strand;
        if (guide.getStrand().equals("+"))
        {
            strand = "1";
        } else {
            strand = "-1";
        }
        coordinates = coordinates + ":" + strand;

        String result = assemblyMapConsumer.executeQuery(coordinates);
        JSONObject json = new JSONObject(result);

        AssemblyDTO assemblyDTO = new AssemblyDTO();
        JSONObject mapped = assemblyMapConsumer.validMapping(json);
        if (mapped != null) {
            assemblyDTO = getAssembly(mapped);
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

    private AssemblyDTO getAssembly(JSONObject mapped)
    {
        AssemblyDTO assemblyDTO = new AssemblyDTO();
        JSONObject map = mapped.getJSONObject("mapped");

        assemblyDTO.setStart(map.getInt("start"));
        assemblyDTO.setChr(map.getString("seq_region_name"));
        assemblyDTO.setStop(map.getInt("end"));
        assemblyDTO.setAssembly(map.getString("assembly"));
        if (map.getInt("strand") == -1)
        {
            assemblyDTO.setStrand("-");
        } else if (map.getInt("strand") == 1) {
            assemblyDTO.setStrand("+");
        }

        return assemblyDTO;
    }
}
