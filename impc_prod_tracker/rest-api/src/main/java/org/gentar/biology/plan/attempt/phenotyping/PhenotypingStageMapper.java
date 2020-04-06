package org.gentar.biology.plan.attempt.phenotyping;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.plan.attempt.phenotyping.stage.status_stamp.PhenotypingStageStatusStamp;
import org.gentar.biology.status.StatusMapper;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PhenotypingStageMapper implements Mapper<PhenotypingStage, PhenotypingStageDTO>
{
    private EntityMapper entityMapper;
    private TissueDistributionMapper tissueDistributionMapper;
    private StatusMapper statusMapper;
    private PhenotypingStageTypeMapper phenotypeStageTypeMapper;

    public PhenotypingStageMapper (
            EntityMapper entityMapper,
            TissueDistributionMapper tissueDistributionMapper,
            StatusMapper statusMapper,
            PhenotypingStageTypeMapper phenotypeStageTypeMapper
    )
    {
        this.entityMapper = entityMapper;
        this.tissueDistributionMapper = tissueDistributionMapper;
        this.statusMapper = statusMapper;
        this.phenotypeStageTypeMapper = phenotypeStageTypeMapper;
    }

    @Override
    public PhenotypingStageDTO toDto(PhenotypingStage phenotypingStage)
    {
        PhenotypingStageDTO phenotypingStageDTO = entityMapper.toTarget(phenotypingStage, PhenotypingStageDTO.class);
        phenotypingStageDTO.setPhenotypingTypeName(phenotypeStageTypeMapper.toDto(phenotypingStage.getPhenotypingStageType()));
        phenotypingStageDTO.setStatusName(phenotypingStage.getStatus().getName());
        addStatusStamps(phenotypingStageDTO, phenotypingStage);
        phenotypingStageDTO.setTissueDistributionCentreDTOs(tissueDistributionMapper.toDtos(phenotypingStage.getTissueDistributions()));
        return phenotypingStageDTO;
    }

    private void addStatusStamps(PhenotypingStageDTO phenotypingStageDTO, PhenotypingStage phenotypingStage) {
        Set<PhenotypingStageStatusStamp> statusStamps = phenotypingStage.getPhenotypingStageStatusStamps();
        List<StatusStampsDTO> statusStampsDTOS = new ArrayList<>();
        if (statusStamps != null) {
            statusStamps.forEach(x -> {
                StatusStampsDTO statusStampsDTO = new StatusStampsDTO();
                statusStampsDTO.setStatusName(x.getStatus().getName());
                statusStampsDTO.setDate(x.getDate());
                statusStampsDTOS.add(statusStampsDTO);
            });
        }
        statusStampsDTOS.sort(Comparator.comparing(StatusStampsDTO::getDate));
        phenotypingStageDTO.setStatusStampsDTOS(statusStampsDTOS);
    }

    @Override
    public PhenotypingStage toEntity(PhenotypingStageDTO dto) {
        PhenotypingStage phenotypingStage =entityMapper.toTarget(dto, PhenotypingStage.class);
        setPhenotypingStageType(phenotypingStage, dto);
        setStatus(phenotypingStage, dto);
        setTissueDistributionCentre(phenotypingStage, dto);
        return phenotypingStage;
    }

    private void setPhenotypingStageType(PhenotypingStage phenotypingStage, PhenotypingStageDTO phenotypingStageDTO)
    {
        phenotypingStage.setPhenotypingStageType(phenotypeStageTypeMapper.toEntity(phenotypingStageDTO.getPhenotypingTypeName()));
    }

    private void setStatus(PhenotypingStage phenotypingStage, PhenotypingStageDTO phenotypingStageDTO)
    {
        phenotypingStage.setStatus(statusMapper.toEntity(phenotypingStageDTO.getStatusName()));
    }

    private void setTissueDistributionCentre(PhenotypingStage phenotypingStage, PhenotypingStageDTO phenotypingStageDTO)
    {
//        TissueDistribution tissueDistributions = tissueDistributionMapper.toEntities(phenotypingStageDTO.getTissueDistributionCentreDTOs());
//        phenotypingStage.setTissueDistributions(tissueDistributions);
    }
    @Override
    public Collection<PhenotypingStage> toEntities(Collection<PhenotypingStageDTO> dtos) {
        return null;
    }
}
