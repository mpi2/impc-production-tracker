package uk.ac.ebi.impc_prod_tracker.service;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.entity.Centre;
import uk.ac.ebi.impc_prod_tracker.data.repository.CentreRepository;

@Component
public class CentreService {

    private CentreRepository centreRepository;

    public CentreService(CentreRepository centreRepository){
        this.centreRepository = centreRepository;
    }

    public Iterable<Centre> getCentres() {
        return centreRepository.findAll();
    }

    public Centre createCentre(Centre centre) {
        return centreRepository.save(centre);
    }
}
