package uk.ac.ebi.impc_prod_tracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.impc_prod_tracker.data.entity.Centre;
import uk.ac.ebi.impc_prod_tracker.service.CentreService;

@RestController
@RequestMapping("/api")
public class CentreController {

    private CentreService centreService;

    public CentreController(CentreService centreService){
        this.centreService = centreService;
    }

    @GetMapping(value = {"/centres"})
    public Iterable<Centre> getCentres()
    {
        return centreService.getCentres();
    }

    @PostMapping(value = {"/centres"}, consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Centre postCentre(@RequestBody Centre centre){
        return centreService.createCentre(centre);
    }

}
