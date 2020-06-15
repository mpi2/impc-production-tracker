package org.gentar.biology.outcome;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.ColonyDTO;
import org.gentar.biology.colony.ColonyMapper;
import org.gentar.biology.colony.distribution.DistributionProduct;
import org.gentar.biology.strain.StrainService;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class ColonyRequestProcessor
{
    private StrainService strainService;
    private ColonyMapper colonyMapper;

    public ColonyRequestProcessor(StrainService strainService, ColonyMapper colonyMapper)
    {
        this.strainService = strainService;
        this.colonyMapper = colonyMapper;
    }

    public Colony getColonyToUpdate(Colony originalColony, ColonyDTO colonyDTO)
    {
        Colony colonyToUpdate = new Colony(originalColony);
        Colony mappedColony = colonyMapper.toEntity(colonyDTO);
        colonyToUpdate.setGenotypingComment(colonyDTO.getGenotypingComment());
        associateDistributionProducts(
            colonyToUpdate, originalColony, mappedColony.getDistributionProducts());
        modifyStrainIfNeeded(colonyToUpdate, colonyDTO);
        return colonyToUpdate;
    }

    private void associateDistributionProducts(
        Colony colonyToUpdate,Colony originalColony, Set<DistributionProduct> distributionProducts)
    {
        if (distributionProducts != null)
        {
            // Colony never changes so we keep the original reference
            distributionProducts.forEach(x -> x.setColony(originalColony));
        }
        colonyToUpdate.setDistributionProducts(distributionProducts);
    }

    private void modifyStrainIfNeeded(Colony colony, ColonyDTO colonyDTO)
    {
        String originalStrainName = colony.getStrain() == null ? "" : colony.getStrain().getName();
        String newStrainName = colonyDTO.getStrainName() == null ? "" : colonyDTO.getStrainName();
        if (!originalStrainName.equals(newStrainName))
        {
            colony.setStrain(strainService.getStrainByNameFailWhenNotFound(colonyDTO.getStrainName()));
        }
    }
}
