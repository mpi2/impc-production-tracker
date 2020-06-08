package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.phenotyping.stage.tissue_distribution.TissueDistribution;
import org.gentar.organization.work_unit.WorkUnitMapper;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TissueDistributionMapper implements Mapper<TissueDistribution, TissueDistributionDTO>
{
    private EntityMapper entityMapper;
    private WorkUnitMapper workUnitMapper;
    private MaterialDepositedTypeMapper materialDepositedTypeMapper;

    public TissueDistributionMapper(EntityMapper entityMapper, WorkUnitMapper workUnitMapper, MaterialDepositedTypeMapper materialDepositedTypeMapper)
    {
        this.entityMapper = entityMapper;
        this.workUnitMapper = workUnitMapper;
        this.materialDepositedTypeMapper = materialDepositedTypeMapper;
    }

    @Override
    public TissueDistributionDTO toDto(TissueDistribution entity)
    {
        return entityMapper.toTarget(entity, TissueDistributionDTO.class);
    }

    @Override
    public List<TissueDistributionDTO> toDtos(Collection<TissueDistribution> tissueDistributions)
    {
        List<TissueDistributionDTO> tissueDistributionDTOS = new ArrayList<>();
        if (tissueDistributions != null){
            tissueDistributions.forEach(tissueDistribution -> tissueDistributionDTOS.add(toDto(tissueDistribution)));
        }
        return tissueDistributionDTOS;
    }

    @Override
    public TissueDistribution toEntity(TissueDistributionDTO dto)
    {
        TissueDistribution tissueDistribution = entityMapper.toTarget(dto, TissueDistribution.class);
        tissueDistribution.setMaterialDepositedType(materialDepositedTypeMapper.toEntity(dto.getMaterialDepositedTypeName()));
        tissueDistribution.setWorkUnit(workUnitMapper.toEntity(dto.getWorkUnitName()));
        return tissueDistribution;
    }

    @Override
    public Set<TissueDistribution> toEntities(Collection<TissueDistributionDTO> dtos)
    {
        Set<TissueDistribution> tissueDistributions = new HashSet<>();
        if (dtos != null)
        {
            dtos.forEach(tissueDistributionDTO -> tissueDistributions.add(toEntity(tissueDistributionDTO)));
        }
        return tissueDistributions;
    }
}
