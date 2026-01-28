package org.gentar.biology.plan.attempt.crispr;

import org.gentar.Mapper;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.EntityMapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.plan.attempt.crispr.mutagenesis_donor.MutagenesisDonor;
import org.gentar.biology.plan.attempt.crispr.mutagenesis_donor.preparation_type.PreparationType;
import org.gentar.biology.plan.attempt.crispr.mutagenesis_donor.preparation_type.PreparationTypeRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MutagenesisDonorMapper implements Mapper<MutagenesisDonor, MutagenesisDonorDTO>
{
    private final EntityMapper entityMapper;
    private final PreparationTypeRepository preparationTypeRepository;

    private static final String PREPARATION_TYPE_NOT_FOUND = "Preparation Type [%s]" +
        " does not exist. Please correct the name or create first the preparation type.";

    public MutagenesisDonorMapper(
        EntityMapper entityMapper, PreparationTypeRepository preparationTypeRepository)
    {
        this.entityMapper = entityMapper;
        this.preparationTypeRepository = preparationTypeRepository;
    }

    public MutagenesisDonorDTO toDto(MutagenesisDonor mutagenesisDonor)
    {
        return entityMapper.toTarget(mutagenesisDonor, MutagenesisDonorDTO.class);
    }

    public List<MutagenesisDonorDTO> toDtos(Collection<MutagenesisDonor> mutagenesisDonors)
    {
        return entityMapper.toTargets(mutagenesisDonors, MutagenesisDonorDTO.class);
    }

    public MutagenesisDonor toEntity(MutagenesisDonorDTO mutagenesisDonorDTO)
    {
        if (mutagenesisDonorDTO == null)
        {
            return null;
        }
        
        MutagenesisDonor mutagenesisDonor = new MutagenesisDonor();
        
        if (mutagenesisDonorDTO.getId() != null && mutagenesisDonorDTO.getId() > 0)
        {
            mutagenesisDonor.setId(mutagenesisDonorDTO.getId());
        }
        
        mutagenesisDonor.setConcentration(mutagenesisDonorDTO.getConcentration());
        mutagenesisDonor.setOligoSequenceFasta(mutagenesisDonorDTO.getOligoSequenceFasta());
        mutagenesisDonor.setVectorName(mutagenesisDonorDTO.getVectorName());
        
        setPreparationTypeToEntity(mutagenesisDonor, mutagenesisDonorDTO);
        return mutagenesisDonor;
    }

    public Set<MutagenesisDonor> toEntities(Collection<MutagenesisDonorDTO> mutagenesisDonorDTOs)
    {
        Set<MutagenesisDonor> mutagenesisDonors = new HashSet<>();
        if (mutagenesisDonorDTOs != null) {
            mutagenesisDonorDTOs.forEach(x -> mutagenesisDonors.add(toEntity(x)));
        }
        return mutagenesisDonors;
    }

    private void setPreparationTypeToEntity(
        MutagenesisDonor mutagenesisDonor, MutagenesisDonorDTO mutagenesisDonorDTO)
    {
        String preparationTypeName = mutagenesisDonorDTO.getPreparationTypeName();
        if (preparationTypeName != null)
        {
            PreparationType preparationType =
                preparationTypeRepository.findFirstByName(preparationTypeName);
            if (preparationType == null)
            {
                String errorMessage =
                    String.format(PREPARATION_TYPE_NOT_FOUND, preparationTypeName);
                throw new UserOperationFailedException(errorMessage);
            }
            mutagenesisDonor.setPreparationType(preparationType);
        }
    }
}
