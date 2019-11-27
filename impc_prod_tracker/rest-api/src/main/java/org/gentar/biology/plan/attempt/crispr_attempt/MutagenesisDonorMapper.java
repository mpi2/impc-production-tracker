package org.gentar.biology.plan.attempt.crispr_attempt;

import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.biology.plan.production.crispr_attempt.MutagenesisDonorDTO;
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
public class MutagenesisDonorMapper
{
    private EntityMapper entityMapper;
    private PreparationTypeRepository preparationTypeRepository;

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
        MutagenesisDonor mutagenesisDonor =
            entityMapper.toTarget(mutagenesisDonorDTO, MutagenesisDonor.class);
        setPreparationTypeToEntity(mutagenesisDonor, mutagenesisDonorDTO);
        removeInvalidId(mutagenesisDonor);
        return mutagenesisDonor;
    }

    public Set<MutagenesisDonor> toEntities(Collection<MutagenesisDonorDTO> mutagenesisDonorDTOs)
    {
        Set<MutagenesisDonor> mutagenesisDonors = new HashSet<>();
        mutagenesisDonorDTOs.forEach(x -> mutagenesisDonors.add(toEntity(x)));
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

    private void removeInvalidId(MutagenesisDonor mutagenesisDonor)
    {
        if (mutagenesisDonor.getId() < 0)
        {
            mutagenesisDonor.setId(null);
        }
    }
}
