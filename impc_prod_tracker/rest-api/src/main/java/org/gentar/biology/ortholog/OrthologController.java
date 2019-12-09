package org.gentar.biology.ortholog;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ortholog")
@CrossOrigin(origins = "*")
public class OrthologController
{
    private OrthologService orthologService;

    public OrthologController(OrthologService orthologService)
    {
        this.orthologService = orthologService;
    }

    @GetMapping(value = {"/byAccIds"})
    Map<String, List<Ortholog>> getOrthologsByAccIds(@RequestParam(value = "accIds") List<String> accIds)
    {
        return orthologService.getOrthologsByAccIds(accIds);
    }
}
