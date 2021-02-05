package org.gentar.report.report_type;

import org.gentar.Mapper;
import org.gentar.report.ReportType;
import org.gentar.report.ReportTypeDTO;
import org.springframework.stereotype.Component;

@Component
public class ReportTypeMapper implements Mapper<ReportType, ReportTypeDTO> {

    @Override
    public ReportTypeDTO toDto(ReportType entity)
    {
        ReportTypeDTO reportTypeDTO = new ReportTypeDTO();
        reportTypeDTO.setName(entity.getName());
        reportTypeDTO.setDescription(entity.getDescription());
        return reportTypeDTO;
    }

}
