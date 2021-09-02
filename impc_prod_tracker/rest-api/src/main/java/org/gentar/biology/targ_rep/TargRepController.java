package org.gentar.biology.targ_rep;

import org.gentar.biology.targ_rep.allele.TargRepAllele;
import org.gentar.biology.targ_rep.allele.TargRepAlleleService;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCell;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCellMapper;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCellService;
import org.gentar.biology.targ_rep.gene.TargRepGene;
import org.gentar.biology.targ_rep.gene.TargRepGeneService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.tomcat.util.IntrospectionUtils.capitalize;

@RestController
@RequestMapping("/api/targ_rep")
@CrossOrigin(origins="*")
public class TargRepController
{
    private final TargRepEsCellMapper esCellMapper;
    private final TargRepAlleleService alleleService;
    private final TargRepEsCellService esCellService;
    private final TargRepGeneService geneService;

    public TargRepController(TargRepEsCellMapper esCellMapper,
                             TargRepAlleleService alleleService,
                             TargRepEsCellService esCellService,
                             TargRepGeneService geneService)
    {
        this.esCellMapper = esCellMapper;
        this.alleleService = alleleService;
        this.esCellService = esCellService;
        this.geneService = geneService;
    }

    /**
     * Get a specific es cell name.
     * @return Entity with the es cell names for a gene.
     */
    @GetMapping(value = {"/es_cell_by_symbol/{marker_symbol}"})
    public List<TargRepEsCellDTO> findEsCellByGene(@PathVariable String marker_symbol)
    {
        TargRepGene gene = geneService.getGeneBySymbolFailIfNull(capitalize(marker_symbol));
        List<TargRepAllele> alleleList = alleleService.getTargRepAllelesByGeneFailIfNull(gene);
        Set<TargRepEsCell> esCellList = alleleList.stream()
                .map(a -> esCellService.getTargRepEscellByAlleleFailsIfNull(a))
                .findAny()
                .stream().flatMap(List::stream).collect(Collectors.toSet());

        return esCellMapper.toDtos(esCellList);
    }

    /**
     * Get a specific es cell name.
     * @return Entity with the es cell names for a gene.
     */
    @GetMapping(value = {"/es_cell_by_name/{name}"})
    public TargRepEsCellDTO findEsCellByName(@PathVariable String name)
    {
        TargRepEsCell esCellList = esCellService.getTargRepEsCellByNameFailsIfNull(name);
        return esCellMapper.toDto(esCellList);
    }

}
