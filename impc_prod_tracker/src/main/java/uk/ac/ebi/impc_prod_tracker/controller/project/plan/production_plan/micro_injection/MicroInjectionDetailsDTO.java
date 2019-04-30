package uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.micro_injection;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MicroInjectionDetailsDTO
{
    private String reference;
    private String mutagenesisFactorReference;
    private String predictedAllele;
    private String blastStrain;
    private String testCrossStrain;
}
