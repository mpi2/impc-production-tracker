package uk.ac.ebi.impc_prod_tracker.web.mapping.plan.attempt.crispr_attempt;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.mutagenesis_donor.MutagenesisDonor;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.production.crispr_attempt.MutagenesisDonorDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.EntityMapper;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MutagenesisDonorMapper
{
    private EntityMapper entityMapper;

    public MutagenesisDonorMapper(EntityMapper entityMapper)
    {
        this.entityMapper = entityMapper;
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
        return entityMapper.toTarget(mutagenesisDonorDTO, MutagenesisDonor.class);
    }

    public Set<MutagenesisDonor> toEntities(Collection<MutagenesisDonorDTO> mutagenesisDonorDTOs)
    {
        return new HashSet<>(entityMapper.toTargets(mutagenesisDonorDTOs, MutagenesisDonor.class));
    }
}
