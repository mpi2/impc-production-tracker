package org.gentar.biology.plan.attempt.phenotyping;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PhenotypingAttemptServiceImpl {

    private final PhenotypingAttemptRepository phenotypingAttemptRepository;

    public PhenotypingAttemptServiceImpl(PhenotypingAttemptRepository phenotypingAttemptRepository)
    {
        this.phenotypingAttemptRepository = phenotypingAttemptRepository;
    }

    public List<PhenotypingAttemptProjectionForMgi> getPhenotypingAttemptProjectionsForMgi(){
        return  phenotypingAttemptRepository.findAllPhenotypingAttemptProjectionsForMgi();
    }
}
