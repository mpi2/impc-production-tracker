package org.gentar.biology.outcome;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.ColonyDTO;
import org.gentar.biology.colony.ColonyMapper;
import org.gentar.biology.colony.ColonyService;
import org.gentar.biology.strain.StrainService;
import org.springframework.stereotype.Component;

@Component
public class ColonyRequestProcessor
{
    private ColonyService colonyService;
    private StrainService strainService;
    private ColonyMapper colonyMapper;

    public ColonyRequestProcessor(
        ColonyService colonyService, StrainService strainService, ColonyMapper colonyMapper)
    {
        this.colonyService = colonyService;
        this.strainService = strainService;
        this.colonyMapper = colonyMapper;
    }

    public Colony getColonyToUpdate(Colony originalColony, ColonyDTO colonyDTO)
    {
        Colony colonyToUpdate = colonyMapper.toEntity(colonyDTO);
        colonyToUpdate.setOutcome(originalColony.getOutcome());
        colonyToUpdate.setStatus(originalColony.getStatus());
        colonyToUpdate.setGenotypingComment(colonyDTO.getGenotypingComment());
        modifyStrainIfNeeded(colonyToUpdate, colonyDTO);
        return colonyToUpdate;
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
