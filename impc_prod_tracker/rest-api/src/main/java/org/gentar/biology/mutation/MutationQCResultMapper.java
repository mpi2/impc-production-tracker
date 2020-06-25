package org.gentar.biology.mutation;

import org.gentar.Mapper;
import org.gentar.biology.mutation.qc_results.MutationQcResult;
import org.gentar.biology.mutation.qc_results.QcStatusService;
import org.gentar.biology.mutation.qc_results.QcTypeService;
import org.springframework.stereotype.Component;

@Component
public class MutationQCResultMapper implements Mapper<MutationQcResult, MutationQCResultDTO>
{
    private QcTypeService qcTypeService;
    private QcStatusService qcStatusService;

    public MutationQCResultMapper(QcTypeService qcTypeService, QcStatusService qcStatusService)
    {
        this.qcTypeService = qcTypeService;
        this.qcStatusService = qcStatusService;
    }

    @Override
    public MutationQCResultDTO toDto(MutationQcResult mutationQcResult)
    {
        MutationQCResultDTO mutationQCResultDTO = new MutationQCResultDTO();
        mutationQCResultDTO.setId(mutationQcResult.getId());
        if (mutationQcResult.getQcType() != null)
        {
            mutationQCResultDTO.setQcTypeName(mutationQcResult.getQcType().getName());
        }
        if (mutationQcResult.getStatus() != null)
        {
            mutationQCResultDTO.setStatusName(mutationQcResult.getStatus().getName());
        }
        return mutationQCResultDTO;
    }

    @Override
    public MutationQcResult toEntity(MutationQCResultDTO mutationQCResultDTO)
    {
        MutationQcResult mutationQcResult = new MutationQcResult();
        mutationQcResult.setId(mutationQCResultDTO.getId());
        if (mutationQCResultDTO.getQcTypeName() != null)
        {
            mutationQcResult.setQcType(
                qcTypeService.getQcTypeByNameFailsIfNull(mutationQCResultDTO.getQcTypeName()));
        }
        if (mutationQCResultDTO.getStatusName() != null)
        {
            mutationQcResult.setStatus(
                qcStatusService.getQcStatusByNameFailsIfNull(mutationQCResultDTO.getStatusName()));
        }
        return mutationQcResult;
    }
}
