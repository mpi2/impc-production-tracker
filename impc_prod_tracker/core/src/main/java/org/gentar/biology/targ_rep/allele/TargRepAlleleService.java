package org.gentar.biology.targ_rep.allele;

import org.gentar.biology.targ_rep.gene.TargRepGene;

import java.util.List;

public interface TargRepAlleleService
{
    List<TargRepAllele> getTargRepAllelesByGeneFailIfNull(TargRepGene gene);
}
